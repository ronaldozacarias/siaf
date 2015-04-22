<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<header class="headerMain">
	<div class="brand">
		<a href="<c:url value="/reserva/ranking" />"><img title="Sistema de Afastamento Docente" alt="Sistema de Afastamento Docente" src="<c:url value="/resources/images/logo-beta.png" />"></a>
	</div>
	
    <nav class="menuTop" role="navigation">
    	<ul role="menubar">
    		<sec:authorize ifAnyGranted="ROLE_DOCENTE,ROLE_ADMIN_SIAF">
	    		<li id="menu-ranking">
	    			<a class="" href="<c:url value="/reserva/ranking" />"><span>Ranking</span></a>
	    		</li>
	    	</sec:authorize>
    		<sec:authorize ifAnyGranted="ROLE_DOCENTE">
	    		<li id="menu-minhas-reservas">
	    			<a class="" href="<c:url value="/reserva/listar" />"><span>Minhas Reservas</span></a>
	    		</li>
	    	</sec:authorize>
    		<sec:authorize ifAnyGranted="ROLE_ADMIN_SIAF">
	    		<li id="menu-periodos">
	    			<a class="" href="<c:url value="/administracao/periodo" />"><span>Gerenciar Períodos</span></a>
	    		</li>
	    		<li id="menu-reservas">
	    			<a class="" href="<c:url value="/administracao/reservas" />"><span>Gerenciar Reservas</span></a>
	    		</li>
	    		<li id="edit-reservas">
	    			<a class="" href="<c:url value="/administracao/alteraReservas" />"><span>Reservas em Aberto</span></a>
	    		</li>
	    		<li id="menu-professores">
	    			<a class="" href="<c:url value="/administracao/professores" />"><span>Professores</span></a>
	    		</li>
    		</sec:authorize>
    		
    		
    		<li id="sair">
    			<a class="" href="<c:url value="/logout" />"><span>Sair</span></a>
    		</li>
    	</ul>
    </nav>
  </header>