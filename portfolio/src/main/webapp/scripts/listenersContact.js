const nCommentsInput = document.querySelector("#nComments");

const defaultNComments = 10;

const templatePromise = loadTemplate('/content/comment.html');

$("document").ready(function() {
  loadNComments(templatePromise, defaultNComments);
});

nCommentsInput.addEventListener('change', () => {
  let nComments = nCommentsInput.value;
  if (nComments < 1) {
    alert("Please enter a positive integer");
    return;
  }
  emptyComments();
  loadNComments(templatePromise, nComments);
});
