$("document").ready(function() {
  fetch('/content/comment.html')
  .then(promiseResponse => promiseResponse.text())
  .then((template) => {
    const htmlTemplate = template;

    fetch('/data')
    .then(promiseResponse => promiseResponse.json())
    .then((commentOsbj) => {
      renderList(htmlTemplate, commentObjs);
    })
  })
})

/**
 * Renders a list of  objects to the DOM, using the specified HTML template
 */
function renderList(template, listObjs) {
  for(let i = 0; i < listObjs.length; i++) {
    let commentHtml = Mustache.render(htmlTemplate, listObjs[i]);
    $('#comments').prepend(commentHtml);
  }
}
