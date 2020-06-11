/**
 * Set the cookie value of an event and call to update the comments.
 */
function updateCommentsAndCookies(event){
  setChangeCookie(event);
  updateCommentSection();
}

/**
 * Update values in the comments section.
 */
function updateCommentSection() {
  let nComments = nCommentsInput.value;
  let sortingParam = sortingParamInput.value;
  let sortingDir = sortingDirInput.value;
  let onlyMyComments = myCommentsCheckbox.checked;
  loadCommentsSection(templatePromise, nComments, sortingParam, sortingDir, onlyMyComments);
}

/**
 * Load comments section.
 */
function loadCommentsSection(templatePromise, nComments, sortingParam, sortingDir, onlyMyComments){
  emptyComments();
  loadNComments(templatePromise, nComments, sortingParam, sortingDir, onlyMyComments)
  .then(addEventListenerComments);
}

/**
 * Loads n number of comments to the Dom based on the given template promise.
 */
function loadNComments(templatePromise, nComments, sortingParam, sortingDir, onlyMyComments){
  let promise = templatePromise.then((template) => {
    promise = loadComments(nComments, sortingParam, sortingDir, onlyMyComments).then((commentObjs) => {
      renderList(template, commentObjs, '#comments');
    });
    return promise;
  });
  return promise;
}

/**
 * Loads the comments from the server.
 * Returns a promise of the comments.
 */
function loadComments(nComments, sortingParam, sortingDir, onlyMyComments) {
  const nCommentsQuery = `nComments=${nComments}`;
  const sortingParamQuery = `sorting=${sortingParam}`;
  const sortingDirQuery = `dir=${sortingDir}`;
  const myComentsQuery = `mine=${onlyMyComments}`;
  const queryParams = [nCommentsQuery, sortingParamQuery, sortingDirQuery, myComentsQuery];
  const queryParamsString = queryParams.join('&');
  const queryString = `/list-comments?${queryParamsString}`;
  const commentsPromise = fetch(queryString).then(promiseResponse => promiseResponse.json());
  return commentsPromise;
}

/**
 * Renders a list of objects to the DOM, using the specified HTML template
 */
function renderList(template, listObjs, parentId) {
  listObjs = listObjs.reverse();
  listObjs.forEach((obj) => {
    obj.date = timestampToDateString(obj.timestamp);
    obj.stars = 'star_border'.repeat(obj.rating);
    obj.sentiment = scoreToSentiment(obj.sentimentScore);
    color = scoreToColor(obj.sentimentScore);
    let html = Mustache.render(template, obj);
    $(parentId).prepend(html);
    let sentimentId = `#${obj.id} > .sentiment-score`;
    $(sentimentId).css('color', color)
  });
}

/**
 * Calculates the date based on the given timestamp in millis.
 */
function timestampToDateString(timestamp) {
  let date = new Date(timestamp);
  dateString = date.toLocaleString();
  return dateString;
}

/**
 * Returns a sentiment description of the given sentiment score.
 */
function scoreToSentiment(score) {
  const sentiments = ['very_dissatisfied', 'dissatisfied', 'neutral', 'satisfied', 'very_satisfied'];
  let index = scoreToIndex(score);
  return sentiments[index];
}

/**
 * Returns a color for the given sentiment score.
 */
function scoreToColor(score) {
  const colors = ['#B22222', '#C66321', '#DAA520', '#7EDD21', '#228B22'];
  let index = scoreToIndex(score);
  return colors[index];
}

/**
 * Returns an integer index by linearly rescaling the scores values in the range (-1, 1),
 * to integers in the range [0, 4]
 */
function scoreToIndex(score) {
  let index = Math.floor((score+1)*2.5);
  index = min(4, index);
  return index;
}

/**
 * Empties the comment section in the DOM.
 */
function emptyComments() {
  $("#comments").empty();
}

/**
 * Deletes the comment with the specified id from datastore.
 */
function deleteComment(id) {
  const params = new URLSearchParams();
  params.append('id', id);
  const request = new Request('/delete-comment', {method: 'POST', body: params});
  fetch(request);
}

/**
 * Deletes all comments from datastore.
 */
function deleteAllComments() {
  const request = new Request('/delete-comment', {method: 'POST'});
  fetch(request);
}
