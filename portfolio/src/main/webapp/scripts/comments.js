/**
 * Loads n number of comments to the Dom based on the given template promise.
 */

function loadNComments(templatePromise, nComments){
  templatePromise.then((template) => {
    loadComments(nComments).then((commentObjs) => {
      renderList(template, commentObjs, '#comments');
    })
  })
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
  for (let i = 0; i < listObjs.length; i++) {
    let html = Mustache.render(template, listObjs[i]);
    $(parentId).prepend(html);
  }
}

function emptyComments() {
  $("#comments").empty();
}

// function deleteComment() {
//   const params = new URLSearchParams();
//   params.append('id', task.id);
//   fetch('/delete-task', {method: 'POST', body: params})
//   .then(() => {

//   })
// }

/**
 * Deletes all comments from datastore.
 */
function deleteAllComments() {
  const request = new Request('/delete-comment', {method: 'POST'});
  fetch(request);
}
