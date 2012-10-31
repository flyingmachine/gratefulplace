var Editable = {
  correspondingContent: function(el) {
    return $(el).parents(".post, .comment").find(".content");
  },
   
  setup: function() {
    // moderation links
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
        $(el).attr("href"),
        {hidden: hidden},
        function() {}
      )

      $(el).text(newText);
      
      ev.preventDefault();
    });
    
    // show the content form
    $(".edit a").click(function(ev) {
      var el = this;
      var content = Editable.correspondingContent(el).get(0)
      content.oldHtml = $(content).html()
      $.get($(el).attr("href"), function(data) {
        $(content).html(data);
      });
      ev.preventDefault();
    });

    
    // cancel
    $(".content").on('click', '.cancel', function(ev) {
      var content = Editable.correspondingContent(this).get(0);
      $(content).html(content.oldHtml);
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
    });

    // formatting help
    $("form, .content").on('click', '.formatting-help a', function(ev) {
      $('.markdown-help').clone().appendTo($(this).parents('form')).removeClass('hidden');
      ev.preventDefault();
    })
  }


}

$(Editable.setup)
;
