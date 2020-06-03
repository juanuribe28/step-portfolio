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
