<%@ page import="formbuilder.ClinicalVariable" %>



<div class="fieldcontain ${hasErrors(bean: clinicalVariableInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="clinicalVariable.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${clinicalVariableInstance?.name}"/>
</div>

