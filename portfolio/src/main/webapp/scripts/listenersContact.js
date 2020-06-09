const nCommentsInput = document.querySelector('#nComments');
const sortingParamInput = document.querySelector("#sorting-param");
const sortingDirInput = document.querySelector("#sorting-dir");
const deleteCommentsButton = document.querySelector('#delete-comments');
const commentForm = document.querySelector("#comment-form")

const templatePromise = loadTemplate('/content/comment.html');

$('document').ready(() => {
  loadNumericValueCookie('nComments');
  loadSelectValueCookie('sorting-param');
  loadSelectValueCookie('sorting-dir')
  updateCommentSection();
  });
nCommentsInput.addEventListener('change', updateCommentsAndCookies);
sortingParamInput.addEventListener('change', updateCommentsAndCookies);
sortingDirInput.addEventListener('change', updateCommentsAndCookies);

commentForm.addEventListener('submit', disableSubmitButton);

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
