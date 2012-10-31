var Favorites = {   
  setup: function() {
    // add favorite

    $("a.favorite").click(function(ev) {
      $.post($(this).attr("href"));
      if ($(this).hasClass("added")) {
        $(this).removeClass("added");
      } else {
        $(this).addClass("added");
      }
      
      ev.preventDefault();
    })
  }


}

$(Favorites.setup)
