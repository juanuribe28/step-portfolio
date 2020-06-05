/**
 * Load comments section.
 */
function loadCommentsSection(templatePromise, nComments){
  emptyComments();
  loadNComments(templatePromise, nComments)
  .then(addEventListenerComments);
}

/**
 * Loads n number of comments to the Dom based on the given template promise.
 */
function loadNComments(templatePromise, nComments){
  let promise = templatePromise.then((template) => {
    promise = loadComments(nComments).then((commentObjs) => {
      renderList(template, commentObjs, '#comments');
    });
    return promise;
  });
  return promise;
}

/**
 * Loads the template from the url.
 * Returns a promise of the template.
 */
function loadTemplate(url) {
  const templatePromise = fetch(url).then(promiseResponse => promiseResponse.text());
  return templatePromise;
}

/**
 * Loads the comments from the server.
 * Returns a promise of the comments.
 */
function loadComments(nComments) {
  const commentsPromise = fetch(`/list-comments?nComments=${nComments}`).then(promiseResponse => promiseResponse.json());
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
    let html = Mustache.render(template, obj);
    $(parentId).prepend(html);
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
