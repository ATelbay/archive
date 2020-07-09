$(document).ready(function() {
  // $("#copyright").click(function(){
  //   $(this).css('color','purple');
  // });
  document.getElementById('copyright').addEventListener("click", function() {
    this.style.color = 'purple';
  });
});
