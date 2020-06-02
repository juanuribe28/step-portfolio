$("document").ready(function() {
  fetch('/content/comment.html')
  .then(promiseResponse => promiseResponse.text())
  .then((template) => {
    const htmlTemplate = template;

    fetch('/data')
    .then(promiseResponse => promiseResponse.json())
    .then((commentObjs) => {
      renderList(htmlTemplate, commentObjs, '#comments');
    })
  })
})

/**
 * Renders a list of  objects to the DOM, using the specified HTML template
 */
function renderList(template, listObjs, parentId) {
  for (let i = 0; i < listObjs.length; i++) {
    let html = Mustache.render(template, listObjs[i]);
    $(parentId).prepend(html);
  }
}
