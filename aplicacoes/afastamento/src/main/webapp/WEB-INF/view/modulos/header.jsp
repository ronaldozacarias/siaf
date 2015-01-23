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
    		<li id="menu-ranking">
    			<a class="" href="<c:url value="/reserva/ranking" />"><span>Ranking</span></a>
    		</li>
    		<li id="menu-add-reserva">
    			<a class="" href="<c:url value="/reserva/incluir" />"><span>Incluir Reserva</span></a>
    		</li>
    		<li id="menu-reservas">
    			<a class="" href="<c:url value="/reserva/listar" />"><span>Minhas Reservas</span></a>
    		</li>
    		<sec:authorize ifAnyGranted="ROLE_ADMIN">
	    		<li id="menu-periodos">
	    			<a class="" href="<c:url value="/administracao/periodo" />"><span>Períodos</span></a>
	    		</li>

	    		<li class="dropdown" id="menu-professores">
	    			<a data-toggle="dropdown" class="dropdown-toggle"><span>Professor</span><b class="caret"></b></a>
                    <ul class="dropdown-menu afas-menu">
                        <li><a href="<c:url value="/administracao/professores" />">Lista professores</a></li>
                        <li><a href="<c:url value="/administracao/novo-professor" />">Novo</a></li>
                    </ul>
	    		</li>
    		</sec:authorize>
    		
    		<li>
    			<a class="" href="<c:url value="/j_spring_security_logout" />"><span>Sair</span></a>
    		</li>
    	</ul>
    	
    </nav>
  </header>