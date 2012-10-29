var Editable = {
  correspondingContent: function(el) {
    return $(el).parents(".post, .comment").find(".content");
  },

  
  
  setup: function() {
    // show the content form
    $(".edit a").click(function(ev) {
      var el = this;
      $.get($(el).attr("href"), function(data) {
        Editable.correspondingContent(el).html(data);
      });
      ev.preventDefault();
    });

    $(".content").on('submit', 'form', function(ev) {
      var el = this;
      $.post(
        $(el).attr("action"),
        $(el).serializeJSON(),
        function(data) {
          console.log(Editable.correspondingContent(el));
          console.log(data);
          Editable.correspondingContent(el).html(data);
        }
      )
      ev.preventDefault();
    })
  }


}

$(Editable.setup)


