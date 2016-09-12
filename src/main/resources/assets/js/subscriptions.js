$('#subscribe-button-img').hover(
    function() {
        $(this).attr('src', 'assets/images/subscribe_button_hover.png');
    },
    function() {
        $(this).attr('src', 'assets/images/subscribe_button_regular.png');
    }
);
$('#subscribe-button-img').click(
    function() {
        $(this).attr('src', 'assets/images/subscribe_button_click.png');
    }
);
$('#sign-in-img').click(
    function() {
        alert('This should take you to a page to link your Swanly and LWA accounts.');
    }
);
$('#dev-info-arrow').click(
    function() {
        $('#dev-info-content').toggle();
    }
)
$('.play').click(
    function() {
        alert("This would play a sample of a song.");
    }
)
$('.play').hover(
    function() {
        $(this).css('opacity', '0.7');
    },
    function() {
        $(this).css('opacity', '1');
    }
);
$('.swanly-dropdown-arrow').hover(
    function() {
        $(this).css('opacity', '0.7');
    },
    function() {
        $(this).css('opacity', '1');
    }
);
