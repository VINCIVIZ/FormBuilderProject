Ext.define('FormBuilder.view.Viewport', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    requires: [
    	'FormBuilder.view.CommonTypeList',
    ],
        
    items: [{
    	title: 'Form Editor',	
    	region: 'center',
    	xtype: 'panel',
    	html: 'Form Editor HTML'
    }, {
    	region: 'west',
    	xtype: 'CommonTypeList',
    	width: '15%',
    	
    	collapsible: true,
    	split: true,
    	
    	html: 'Clinial Variables HTML'
    }],
    
    /*initComponent: function() {
    	this.items = {
    		
    	}
    	
    	this.callParent();
    }*/
    
    /*requires: [
        'Pandora.view.NewStation',
        'Pandora.view.SongControls',
        'Pandora.view.StationsList',
        'Pandora.view.RecentlyPlayedScroller',
        'Pandora.view.SongInfo'
    ],
    
    initComponent: function() {
        this.items = {
            dockedItems: [{
                dock: 'top',
                xtype: 'toolbar',
                height: 80,
                items: [{
                    xtype: 'newstation',
                    width: 150
                }, {
                    xtype: 'songcontrols',
                    flex: 1
                }, {
                    xtype: 'component',
                    html: 'Pandora<br>Internet Radio'
                }]
            }],
            layout: {
                type: 'hbox',
                align: 'stretch'
            },
            items: [{
                width: 250,
                xtype: 'panel',
                id: 'west-region',
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: 'stationslist',
                    flex: 1
                }, {
                    html: 'Ad',
                    height: 250,
                    xtype: 'panel'
                }]
            }, {
                xtype: 'container',
                flex: 1,
                layout: {
                    type: 'vbox',
                    align: 'stretch'
                },
                items: [{
                    xtype: 'recentlyplayedscroller',
                    height: 250
                }, {
                    xtype: 'songinfo',
                    flex: 1
                }]
            }]
        };
        
        this.callParent();
    }*/
});