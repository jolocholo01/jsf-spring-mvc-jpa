<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Sistema de Gestão Escolar</title>
<c:set var="path" value="${pageContext.request.contextPath}"
	scope="request" />
<!-- Bootstrap core CSS -->
<meta name="viewport" content="width=device-width, initial-scale=1" />

<!--===============================================================================================-->
<link rel="icon" type="image/png" href="${path}/resources/images/favicon.ico" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/bootstrap/css/bootstrap.min.css" />

<!--===============================================================================================-->
<link rel="stylesheet" type="text/css" href="${path}/resources/css/login.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/bootstrap/fonts/font-awesome-4.7.0/css/font-awesome.min.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/vendor/animate/animate.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/vendor/css-hamburgers/hamburgers.min.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/vendor/animsition/css/animsition.min.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/vendor/select2/select2.min.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/vendor/daterangepicker/daterangepicker.css" />
<!--===============================================================================================-->
<link rel="stylesheet" type="text/css"
	href="${path}/resources/bootstrap/css/util.css" />
<link rel="stylesheet" type="text/css"
	href="${path}/resources/bootstrap/css/main.css" />
<!--===============================================================================================-->
</head>

<body>


	<div class="limiter">
		<div class="container-login100">
			<div class="wrap-login100">
				<c:url value="/login" var="loginUrl" />
				<form action="${loginUrl}" method="post"
					class="login100-form validate-form p-l-55 p-r-55 p-t-178 formulario-login">
					<span class="login100-form-title"> Sistema de Gestão Escolar
					</span>
					<c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null}">
						<ul style="margin-left: 4%;">
							<li style="color: #F00; text-align: center;">
								${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</li>
						</ul>
					</c:if>

					<div class="wrap-input100 validate-input m-b-16"
						data-validate="O campo usuário é obrigatório!">
						<input id="username" class="input100 usuario" type="text"
							name="username" placeholder="Usuário"> </input> <span
							class="focus-input100"> </span>
					</div>

					<div class="wrap-input100 validate-input"
						data-validate="O campo senha é obrigatório!">
						<input class="input100 senha" id="password" type="password"
							name="password" placeholder="Senha"></input> <span
							class="focus-input100"></span>
					</div>

					<div class="text-right p-t-13 p-b-23">
						<a href="${path}/senha.jsf" class="txt2"> Esqueci senha </a>
					</div>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
					<div class="container-login100-form-btn">
						<button type="submit" class="login100-form-btn">Autenticar</button>
					</div>


					<div class="flex-col-c p-t-110 p-b-40">
						<span class="txt1 p-b-9"> ©&#160;<span id="tempo"></span>&#160;&#160;|&#160;
							SIGE



						</span>
					</div>
				</form>
			</div>
		</div>
	</div>


	<!--===============================================================================================-->
	<script src="${path}/resources/vendor/jquery/jquery-3.2.1.min.js"></script>
	<!--===============================================================================================-->
	<script src="${path}/resources/vendor/animsition/js/animsition.min.js"></script>
	<!--===============================================================================================-->
	<script src="${path}/resources/vendor/bootstrap/js/popper.js"></script>
	<script src="${path}/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
	<!--===============================================================================================-->
	<script src="${path}/resources/vendor/select2/select2.min.js"></script>
	<!--===============================================================================================-->
	<script src="${path}/resources/vendor/daterangepicker/moment.min.js"></script>
	<script src="${path}/resources/vendor/daterangepicker/daterangepicker.js"></script>
	<!--===============================================================================================-->
	<script src="${path}/resources/vendor/countdowntime/countdowntime.js"></script>
	<!--===============================================================================================-->
	<script src="${path}/resources/js/main.js"></script>


</body>
</html>
