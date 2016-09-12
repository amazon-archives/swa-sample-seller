$('#sign-in-img').hover(
    function() {
        $(this).attr('src', 'assets/images/sign_in_hover.png');
    },
    function() {
        $(this).attr('src', 'assets/images/sign_in_regular.png');
    }
);
$('#sign-in-img').click(
    function() {
        $(this).attr('src', 'assets/images/sign_in_click.png');
    }
);
$('#sign-in-img').click(
    function() {
        alert('This should take you to a page to link your Swanly and LWA accounts.');
    }
);
