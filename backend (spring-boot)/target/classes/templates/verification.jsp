<!DOCTYPE HTML>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>VivaTech - OTP Confirmation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .logo {
            text-align: center;
            margin-bottom: 20px;
        }

        .logo img {
            width: 150px;
        }

        .content {
            text-align: left;
        }

        .footer {
            margin-top: 20px;
            text-align: center;
            color: #888;
        }
    </style>
</head>

<body>
    <div class="container">
        <div class="logo">
            <!--  <img src="path/to/your/logo.png" alt="VivaTech Logo"> -->
        </div>
        <div class="content">
            <h2>OTP Confirmation for VivaTech</h2>
            <p>Dear User,</p>
            <p>Thank you for using VivaTech! Use the following One-Time Password (OTP) to verify your identity:</p>
            <h3>Your OTP: <span style="color: #007bff;">${OTP}</span></h3>
            <p>This OTP is valid for 10 minutes.</p>
            <p>If you didn't request this OTP, please ignore this email.</p>
            <p>Best regards,<br> VivaTech Team</p>
        </div>
        <div class="footer">
            <p>© 2023 VivaTech. All rights reserved.</p>
        </div>
    </div>
</body>

</html>
