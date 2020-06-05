const nCommentsInput = document.querySelector('#nComments');
const sortingParamInput = document.querySelector("#sorting-param");
const sortingDirInput = document.querySelector("#sorting-dir");
const deleteCommentsButton = document.querySelector('#delete-comments');

const templatePromise = loadTemplate('/content/comment.html');

let nComments = nCommentsInput.value;
let sortingParam = sortingParamInput.value;
let sortingDir = sortingDirInput.value;

$('document').ready(() => {
  loadCommentsSection(templatePromise, nComments, sortingParam, sortingDir);
});

nCommentsInput.addEventListener('change', () => {
  nComments = nCommentsInput.value;
  if (nComments < 1) {
    alert('Please enter a positive integer');
    return;
  }
  loadCommentsSection(templatePromise, nComments, sortingParam, sortingDir);
});

sortingParamInput.addEventListener('change', () => {
  sortingParam = sortingParamInput.value;
  loadCommentsSection(templatePromise, nComments, sortingParam, sortingDir);
});

sortingDirInput.addEventListener('change', () => {
  sortingDir = sortingDirInput.value;
  loadCommentsSection(templatePromise, nComments, sortingParam, sortingDir);
});

deleteCommentsButton.addEventListener('click', () => {
  emptyComments();
  deleteAllComments();
});

function addEventListenerComments(){
  const comments = document.querySelectorAll('.comment-section');
  comments.forEach((comment) => {
    const deleteX = comment.querySelector('.delete');
    deleteX.addEventListener('click', () => {
      deleteComment(comment.id);
      loadCommentsSection(templatePromise, nComments, sortingParam);
    }); 
  });
}
