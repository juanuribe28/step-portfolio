$("document").ready(function() {
  let headerTemplatePromise = loadTemplate('content/header.html');
  let formTemplatePromise = loadTemplate('content/commentForm.html');
  let objPromise = loadObject('/auth');
  Promise.all([headerTemplatePromise, formTemplatePromise, objPromise]).then((values) => {
    let headerTemplate = values[0];
    let formTemplate = values[1];
    let obj = values[2];
    let htmlHeader = Mustache.render(headerTemplate, obj);
    let htmlForm = Mustache.render(formTemplate, obj);
    $("header").html(htmlHeader);
    $("div#form").html(htmlForm);
  });
  $("footer").load('content/footer.html').addClass('center');
})


