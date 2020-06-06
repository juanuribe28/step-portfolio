const nCommentsInput = document.querySelector('#nComments');
const sortingParamInput = document.querySelector("#sorting-param");
const sortingDirInput = document.querySelector("#sorting-dir");
const deleteCommentsButton = document.querySelector('#delete-comments');

const templatePromise = loadTemplate('/content/comment.html');

$('document').ready(updateCommentSection);
nCommentsInput.addEventListener('change', updateCommentSection);
sortingParamInput.addEventListener('change', updateCommentSection);
sortingDirInput.addEventListener('change', updateCommentSection);

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
      updateCommentSection();
    }); 
  });
}
