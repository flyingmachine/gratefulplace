var Editable = {
  correspondingContent: function(el) {
    return $(el).parents(".post, .comment").find(".content");
  },
  
  setup: function() {
    $(".edit a").click(function(ev) {
      var node = this;
      $.get($(node).attr("href"), function(data) {
        Editable.correspondingContent(node).html(data);
      });
      return false;
    });
  }
}

$(Editable.setup)


