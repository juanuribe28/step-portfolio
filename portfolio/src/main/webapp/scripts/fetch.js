$("document").ready(function() {
  fetch('/content/comment.html')
  .then(promiseResponse => promiseResponse.text())
  .then((template) => {
    const htmlTemplate = template;

    fetch('/data')
    .then(promiseResponse => promiseResponse.json())
    .then((commentJson) => {
      let commentHtml = Mustache.render(htmlTemplate, commentJson);
      $('#comments').prepend(commentHtml);
    })
  })
})