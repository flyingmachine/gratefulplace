var Editable = {
  correspondingContent: function(el) {
    return $(el).parents(".post, .comment").find(".content");
  },

  
  
  setup: function() {
    $(".moderate a").click(function(ev) {
      var el = this;

      if ($(el).text() == "hide") {
        var hidden = true;
        var newText = "unhide";
      } else {
        var hidden = false;
        var newText = "hide";
      }
      
      $.post(
        $(el).attr("action"),
        {hidden: hidden},
        function() {}
      )

      $(el).text(newText);
      
      ev.preventDefault();
    });
    
    // show the content form
    $(".edit a").click(function(ev) {
      var el = this;
      $.get($(el).attr("href"), function(data) {
        Editable.correspondingContent(el).html(data);
      });
      ev.preventDefault();
    });

    // content form submission
    $(".content").on('submit', 'form', function(ev) {
      var el = this;
      $.post(
        $(el).attr("action"),
        $(el).serializeJSON(),
        function(data) {
          Editable.correspondingContent(el).html(data);
        }
      )
      ev.preventDefault();
    })
  }


}

$(Editable.setup)


;
