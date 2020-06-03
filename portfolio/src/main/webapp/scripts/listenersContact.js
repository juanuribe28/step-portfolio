const nCommentsInput = document.querySelector("#nComments");

const defaultNComments = 10;

const templatePromise = loadTemplate('/content/comment.html');

$("document").ready(function() {
  loadNComments(templatePromise, defaultNComments);
});

nCommentsInput.addEventListener('change', () =>{
  emptyComments();
  let nComments = nCommentsInput.value;
  loadNComments(templatePromise, nComments);
});
