$("document").ready(function() {
  fetch('/data').then(promiseResponse => promiseResponse.text()).then((comment) => {
    $('#content').html(comment);
  })
})