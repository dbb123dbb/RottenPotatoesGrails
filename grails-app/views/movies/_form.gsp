<%@ page import="com.dennisbeasley.RottenPotatoes.Movies" %>



<div class="fieldcontain ${hasErrors(bean: moviesInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="movies.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${moviesInstance?.title}"/>
</div>

