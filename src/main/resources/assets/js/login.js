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
        alert('This would log you in with Swanly\'s non-LWA account system. This should take you to a page to link your Swanly and LWA accounts.');
    }
);
$('#terms-of-use').click(
    function() {
        alert('This would open a page containing your Terms of Use.');
    }
);
$('#privacy-policy').click(
    function() {
        alert('This would open a page containing your Privacy Policy.');
    }
);
