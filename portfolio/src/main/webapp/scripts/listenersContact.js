let nComments = 3;

const templatePromise = loadTemplate('/content/comment.html');

$("document").ready(function() {
  templatePromise.then((template) => {
    loadComments(nComments).then((commentObjs) => {
      renderList(template, commentObjs, '#comments');
    })
  })
})
