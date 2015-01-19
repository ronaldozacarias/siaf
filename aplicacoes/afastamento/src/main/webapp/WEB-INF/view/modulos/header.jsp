<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="headerMain">
	<div class="brand">
		<img alt="Sistema de Afastamento de Professores" src="<c:url value="/resources/images/logo.png" />">
	</div>
	
    <div class="accessTop"></div>
    
    <nav class="menuTop" role="navigation">
    	<ul role="menubar">
    		<li>
    			<a class="" href="<c:url value="/reserva/ranking" />"><span>Ranking</span></a>
    		</li>
    		<li>
    			<a class="" href="<c:url value="/reserva/incluir" />"><span>Incluir Reserva</span></a>
    		</li>
    		
    		<sec:authorize ifAnyGranted="ROLE_ADMIN">
	    		<li>
	    			<a class="" href="<c:url value="/administracao/professores" />"><span>Professores</span></a>
	    		</li>
    		</sec:authorize>
    		
    		<li>
    			<a class="" href="<c:url value="/j_spring_security_logout" />"><span>Sair</span></a>
    		</li>
    	</ul>
    </nav>
  </header>