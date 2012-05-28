

Ext.Loader.setConfig({enabled:true});

Ext.application({
    name: 'FormBuilder',
    
    autoCreateViewport: true,
    
    models: ['CommonType', 'FormEditor'],
    stores: ['CommonType'],
    controllers: ['CommonTypeList', 'FormEditor']
});

