<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Asynchronous Form</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .results {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
      }
      .form-repeat {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-repeat .form-repeat-heading,
      .form-repeat .checkbox {
        margin-bottom: 10px;
      }
      .form-repeat input[type="text"],
      .form-repeat input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
    <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

  </head>

  <body>

    <div class="container">

      <form class="form-repeat">
        <h2 class="form-repeat-heading">Repeat Some Text</h2>
        <input type="text" class="input-block-level" name="torepeat" placeholder="Text to repeat">
        <button class="btn btn-large btn-primary">Repeat</button>
      </form>
      <div class="results">
      <div>
      
    </div> <!-- /container -->


    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="bootstrap/js/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(function() {
            jQuery('.btn').click(function(e) {
                e.preventDefault()
                jQuery.get('rest/repeat/' + jQuery('[name=torepeat]').val(),
                    function(data) {
                        jQuery('.results').html(
        '<div class="alert alert-success">'
           + '<button type="button" class="close" data-dismiss="alert">&times;</button>'
            + '<p class="alert-body">' + data.text + '</p>'
            + '</div>'
        )}, 'json')
            })
        })
    </script>
  </body>
</html>
