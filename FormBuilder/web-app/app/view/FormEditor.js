Ext.define('FormBuilder.view.FormEditor', {
	extend:'Ext.panel.Panel',
	alias: 'widget.FormEditor',
	
    requires: [
		'FormBuilder.view.VariableFieldSet',
	],
	
	bodyPadding: 10, 
	autoScroll: true,
	 
	title: 'Form Editor', 
	layout: 'anchor',
	
	defaultType: 'textfield',
	schemaName : '',

	dockedItems: [{
		xtype: 'toolbar',
		dock: 'top',
		items: [{
			xtype: 'button',
			text: 'Save',
			handler: function() {
				view = this.up('FormEditor');
				Ext.Msg.prompt('Save Schema', "Enter schema name: ", function(btn, text) {
					if (btn != 'ok') {
						return;
					}
					this.schemaName = text;
					this.controller.saveSchema(text);
				}, view, false, view.schemaName);
			},
		}],
	}],

    listeners : {
    	render : function() {
    		this.dropZone = new Ext.dd.DropZone(this.body, {
    			view: this,
    			ddGroup: 'elementDdGroup',
    			
    			getTargetFromEvent: function(e) {		
		            return e.target; 
		        },
		       	        
		        onNodeDrop: function(target, dd, e, data) {
		        	this.view.controller.addElement(data.records[0].data.name);
		        },
    		});
    	}
    }
});
