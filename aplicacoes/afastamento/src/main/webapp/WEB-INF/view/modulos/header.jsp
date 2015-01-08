<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<header class="headerMain">
	<div class="brand">
		<img alt="Sistema de Afastamento de Professores" src="<c:url value="/resources/images/logo.png" />">
	</div>
	
    <div class="accessTop"></div>
    
    <nav class="menuTop" role="navigation">
    	<ul role="menubar">
    		<li role="presentation">
    			<a class="" href="#"><span>Ranking</span></a>
    		</li>
    		<li role="presentation">
    			<a class="" href="<c:url value="/afastamento/solicitar" />"><span>Solicitar Reserva de Afastamento</span></a>
    		</li>
    		<li role="presentation">
    			<a class="" href="#"><span>Professores Afastados</span></a>
    		</li>
    		<li role="presentation">
    			<a class="" href="#"><span>Sair</span></a>
    		</li>
    	</ul>
    </nav>
  </header>