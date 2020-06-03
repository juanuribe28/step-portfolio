const templatePromise = loadTemplate('/content/comment.html');

$("document").ready(function() {
  templatePromise.then((template) => {
    loadComments(template)
    .then((commentObjs) => {
      renderList(template, commentObjs, '#comments');
    })
  })
})
