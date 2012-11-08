var Favorites = {   
  setup: function() {
    // add favorite

    $(".favorite").each(
      function(i, fav){
        var status = $(fav).children(".status");
        $(fav).children("a").click(function(ev) {
          $.post($(this).attr("href"));
          if ($(fav).hasClass("added")) {
            $(fav).removeClass("added");
            $(this).attr("href", $(this).attr("href").replace("/destroy", ""));
            $(status).text("Like this");
          } else {
            $(fav).addClass("added");
            $(this).attr("href", $(this).attr("href") + "/destroy");
            $(status).text("You like this");
          }
          ev.preventDefault();
        })
      })
  }
}

$(Favorites.setup)
