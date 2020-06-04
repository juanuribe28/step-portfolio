const nCommentsInput = document.querySelector('#nComments');
const deleteCommentsButton = document.querySelector('#deleteComments');

const templatePromise = loadTemplate('/content/comment.html');

let nComments = 10;

$('document').ready(() => {
  loadCommentsSection(templatePromise, nComments);
});

nCommentsInput.addEventListener('change', () => {
  nComments = nCommentsInput.value;
  if (nComments < 1) {
    alert('Please enter a positive integer');
    return;
  }
  loadCommentsSection(templatePromise, nComments);
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
      loadCommentsSection(templatePromise, nComments);
    }); 
  });
}
