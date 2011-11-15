<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet"  type="text/css" href="resources/editor/css/start/jquery-ui-1.8rc2.custom.css"  />
<link type="text/css" rel="stylesheet" href="resources/editor/css/demo_table_jui.css" /> 
<link type="text/css" rel="stylesheet" href="resources/editor/css/editor.css" /> 
<script type="text/javascript" src="resources/editor/js/jquery.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.json-2.3.min.js"></script>
<!-- <script type="text/javascript" src="resources/editor/js/jquery.ui.selectmenu.js"></script> -->
<!-- <script type="text/javascript" src="resources/editor/js/jquery-dynamic-form.js"></script> -->
<script type="text/javascript" src="resources/editor/js/main.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.tmpl.js"></script>
<script type="text/javascript" src="resources/editor/js/knockout-1.3.0beta.js"></script>
<script type="text/javascript" src="resources/editor/js/knockout.mapping-latest.js"></script>
<script type="text/javascript" src="resources/editor/js/editor.js"></script>

  <script>
 
  $(document).ready(function(){
    $('#switcher').themeswitcher();
  });
 
$(function(){

$("form").form();

});

</script>


<script id='entityDescriptionTemplate' type='text/html'>
<label for="description">Description </label>
{{if codeSystemCatalogEntry.resourceSynopsis != null}}
	<input type="text" name="description" id="description" data-bind="value: csViewModel.codeSystemCatalogEntry.resourceSynopsis.value.content" class="text ui-widget-content ui-corner-all" />
{{else}}
 	<a href="#" data-bind="click: function() { csViewModel.addDescription() }">Add Description</a>
{{/if}}
</script>

<script id='keywordTemplate' type='text/html'>
<input name="keyword" id="keyword" type="text" data-bind="value: keyword"/>
<br></br>
</script>

<script id='keywordDisplayTemplate' type='text/html'>
  <span data-bind="text: keyword"></span>
<br></br>
</script>

</head>

<body>
Select A Theme: <div id="switcher"></div>
<br /><br />


<div id="createChangeSetForm" title="Create ChangeSet">

	<form id="changeSetEditForm" class="ui-widget">
		<fieldset class="ui-widget-content" title="Keywords">
			<legend class="ui-widget-header ui-corner-all">Change Instructions</legend>
			<label for="keyword">Change Instructions </label>
			<input type="text" name="changeInstructions" id="changeInstructions" class="text ui-widget-content ui-corner-all" />
		</fieldset>
	</form>

</div>

<div id="resourceTabs">

	<ul>
		<li><a href="#editCodeSystem">CodeSystems</a></li>		
		<li><a href="#editEntity">Entities</a></li>		
		<li><a href="#changeSetTab">Change Sets</a></li>		
	</ul>
	
	<div id="changeSetTab">
		<input type="submit" value="Rollback" onclick="rollbackChangeSet();"/>
		<input type="submit" value="Commit" onclick="commitChangeSet();"/>
		<br></br>
		<input type="submit" value="Create New ChangeSet" onclick="createNewChangeSet();"/>
	   	<table id="changeSetTable" class="display">
              <thead>
                <tr>
                  <th>
                    ChangeSetUri
                  </th>
                  <th>
                    Creation Date
                  </th>
                  <th>
                    Change Instructions
                  </th>
                  <th>
                    Status
                  </th>
                </tr>
              </thead>
            </table>
	</div>

<div id="editCodeSystem">

	<ul>
		<li><a href="#cs-tabs-1">Edit</a></li>
		<li><a href="#cs-tabs-2">Create New</a></li>
		<li><a href="#cs-tabs-4">Search</a></li>
		
	</ul>
	
	<div id="codeSystemEditForm" title="Edit CodeSystem">

		<label id="codeSystemLNameLabel"><b>CodeSystemName:</b> <span data-bind="text: codeSystemCatalogEntry.codeSystemName" ></span></label>
		<br></br>
		<label id="aboutLabel"><b>About:</b> <span data-bind="text: codeSystemCatalogEntry.about"></span></label>
		<br></br>
		<form id="editForm" class="ui-widget">
		
			<fieldset class="ui-widget-content" title="Edit">
				<legend class="ui-widget-header ui-corner-all">Description</legend>
				<div data-bind='template: { name: "entityDescriptionTemplate" }'> </div>
			</fieldset>
			<fieldset id="cs-chooseChangeSetForEditFieldset" class="ui-widget-content" title="ChangeSet">
				<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
				<label for="cs-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
				<select id="cs-edit-choose-changeSetDropdown" name="cs-edit-choose-changeSetDropdown"></select>
			</fieldset>
			
			<fieldset class="ui-widget-content" title="Keywords">
				<legend class="ui-widget-header ui-corner-all">KeyWords</legend>
				<a href="#" data-bind="click: function() { viewModel.addKeyword() }">Add Keyword</a>
				<div data-bind='template: { name: "keywordTemplate", foreach: csViewModel.codeSystemCatalogEntry.keywordList }'></div>
			</fieldset>
		</form>
	
	</div>

	<div id="cs-tabs-1">

		
		<label for="cs-edit-search-changeSetDropdown">ChangeSet: </label>
		<select id="cs-edit-search-changeSetDropdown" 
			name="cs-edit-search-changeSetDropdown" 
			data-getAllFunctionName="onListAllCodeSystems"
			class="AddCurrentOption"></select>
			
		<br></br>	
		
		<label for="cs-editAutocomplete">Search: </label>
		<input id="cs-editAutocomplete"/>
		<input type="submit" id="clearSearch" value="Clear Search" name="clearSearch" onclick="onClearEditSearch()"/>
	
		<br></br>
						
		<table id="codeSystemTable" class="display">
              <thead>
                <tr>
                  <th>
                    CodeSystem Name
                  </th>
                  <th>
                    About
                  </th>
                   <th>
                    Description
                  </th>
                </tr>
              </thead>
            </table>
	</div>
	<div id="cs-tabs-2">
	
		<label for="cs-create-changeSetDropdown">ChangeSet: </label>
		<select id="cs-create-changeSetDropdown">
			</select>
			
		<form style="width:50%">
		
			<fieldset>
			<legend>Create</legend>
			<table>
			<tr><td>Code System Name </td><td><input id="csn" name="csn" type="text"  /></td></tr>
			<tr><td>About </td><td><input id="about" name="about" type="text"  /></td></tr>
	
			
			<tr><td><input type="submit" value="Create" onclick="create();"/></td></tr>
			
			</table>
			</fieldset>
		</form>
	</div>

		<div id="cs-tabs-4">
		
		<label for="cs-search-changeSetDropdown">ChangeSet: </label>
		<select id="cs-search-changeSetDropdown" name="cs-search-changeSetDropdown" class="AddCurrentOption"></select>

		<input id="cs-searchAutocomplete"/>
				
		<br></br>	
						
		<table id="cs-autocompleteTable" class="display">
              <thead>
                <tr>
                  <th>
                    CodeSystem Name
                  </th>
                </tr>
              </thead>
            </table>
	</div>
</div>

<div id="editEntity">

	<ul>
		<li><a href="#ed-tabs-1">Edit</a></li>
		<li><a href="#ed-tabs-2">Create New</a></li>
		<li><a href="#ed-tabs-4">Search</a></li>
		
	</ul>
	
	<div id="entityEditForm" title="Edit Entity">

		<label id="entityNamespaceLabel"><b>Entity Namespace:</b> <span data-bind="text: entityDescription.namedEntity.entityID.namespace" ></span></label>
		<br></br>
		<label id="entityNameLabel"><b>Entity Name:</b> <span data-bind="text: entityDescription.namedEntity.entityID.name"></span></label>
		<br></br>
		<table>
	        <thead>
	            <tr>
	                <th>Description</th>
	            </tr>
	        </thead>
	        <tbody data-bind='template: { name: "designationRowTemplate" }'></tbody>
   	 	</table>
		<a href="#" data-bind="click: function() { addDesignation() }">Add Description</a>
	
	</div>
	
	<script type="text/html" id="designationRowTemplate">
	{{if entityDescription.namedEntity.designationList.length != 0}}
		<input type="text" name="designation" id="designation" data-bind="value: entityDescription.namedEntity.designationList[0].value.content" class="text ui-widget-content ui-corner-all" />
		{{else}}
		No Descriptions
	{{/if}}
	</script>

	<div id="ed-tabs-1">

		
		<label for="ed-edit-search-changeSetDropdown">ChangeSet: </label>
		<select id="ed-edit-search-changeSetDropdown" 
			name="ed-edit-search-changeSetDropdown" 
			data-getAllFunctionName="onListAllEntities"
			class="AddCurrentOption"></select>
			
		<br></br>	
		
		<label for="ed-editAutocomplete">Search: </label>
		<input id="ed-editAutocomplete"/>
		<input type="submit" id="ed-clearSearch" value="Clear Search" name="ed-clearSearch" onclick="onClearEditSearch()"/>
	
		<br></br>
						
		<table id="entityTable" class="display">
              <thead>
                <tr>
                  <th>
                    Entity Namespace
                  </th>
                  <th>
                    Entity Name
                  </th>
                   <th>
                    Description
                  </th>
                   <th>
                    Code System
                  </th>
                   <th>
                    Code System Version
                  </th>
                </tr>
              </thead>
            </table>
	</div>
	<div id="ed-tabs-2">
	
		<label for="ed-create-changeSetDropdown">ChangeSet: </label>
		<select id="ed-create-changeSetDropdown">
			</select>
			
		<form style="width:50%">
		
			<fieldset>
			<legend>Create</legend>
			<table>
			<tr><td>Entity Name </td><td><input id="entityName" name="entityName" type="text"  /></td></tr>
			<tr><td>Entity Namespace </td><td><input id="entityNamespace" name="entityNamespace" type="text"  /></td></tr>
			<tr><td>About </td><td><input id="entityNameAbout" name="entityNameAbout" type="text"  /></td></tr>
	
			
			<tr><td><input type="submit" value="Create" onclick="createEntity();"/></td></tr>
			
			</table>
			</fieldset>
		</form>
	</div>

		<div id="ed-tabs-4">
		
		<label for="ed-search-changeSetDropdown">ChangeSet: </label>
		<select id="ed-search-changeSetDropdown" name="ed-search-changeSetDropdown" class="AddCurrentOption"></select>

		<input id="ed-searchAutocomplete"/>
				
		<br></br>	
						
		<table id="ed-autocompleteTable" class="display">
              <thead>
                <tr>
                  <th>
                    CodeSystem Name
                  </th>
                </tr>
              </thead>
            </table>
	</div>
</div>

</div>

<textarea class="ui-widget ui-state-default ui-corner-all" id="logmsgs" readonly="readonly" rows="5">
</textarea> 

<script type="text/javascript"
  src="http://jqueryui.com/themeroller/themeswitchertool/">
</script>


</body>
</html>
