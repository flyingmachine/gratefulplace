var Editable = {
  correspondingContent: function(el) {
    return $(el).parents(".post, .comment").find(".content");
  },

  
  
  setup: function() {
    $(".moderate form").submit(function(ev) {
      var el = this;
      $.post(
        $(el).attr("action"),
        $(el).serializeJSON(),
        function() {}
      )

      var field = $(el).children("input[type=hidden]");
      var submit = $(el).children("input[type=submit]");
      
      if (field.val() == "true") {
        field.val("false");
        submit.val("unhide");
      } else {
        field.val("true");
        submit.val("hide");
      }
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


