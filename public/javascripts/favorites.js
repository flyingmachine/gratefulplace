var Favorites = {   
  setup: function() {
    // add favorite

    $("a.favorite").click(function(ev) {
      $.post($(this).attr("href"));
      if ($(this).hasClass("added")) {
        $(this).removeClass("added");
        $(this).attr("href", $(this).attr("href").replace("/delete", ""));
      } else {
        $(this).addClass("added");
        $(this).attr("href", $(this).attr("href") + "/delete");
      }
      
      ev.preventDefault();
    })
  }


}

$(Favorites.setup)
;
