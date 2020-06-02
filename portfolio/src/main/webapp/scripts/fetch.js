$("document").ready(function() {
  fetch('/content/comment.html')
  .then(promiseResponse => promiseResponse.text())
  .then((template) => {
    const htmlTemplate = template;

    fetch('/data')
    .then(promiseResponse => promiseResponse.json())
    .then((commentsObj) => {
      for(let i = 0; i < commentsObj.length; i++) {
        let commentHtml = Mustache.render(htmlTemplate, commentsObj[i]);
        $('#comments').prepend(commentHtml);
      }
    })
  })
})
