/**
 * @author sdaskaliesku
 */
$(document).ready(function () {

    var clanContainer = $('#clanContainer');
    var personalContainer = $('#personalContainer');
    var blackListContainer = $('#blackListContainer');
    var versionContainer = $('#versionContainer');
    var logsContainer = $('#logsContainer');

    var containers = [clanContainer, personalContainer, blackListContainer, versionContainer, logsContainer];

    clanContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Clans',
        actions: {
            listAction: '/crud/accessList/clans',
            createAction: '/crud/accessList/create',
            updateAction: '/crud/accessList/update',
            deleteAction: '/crud/accessList/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            name: {
                title: 'Clan Name',
                inputClass: 'validate[required]'
            },
            fromDate: {
                title: 'Date begin',
                type: 'date'
            },
            dueDate: {
                title: 'Date end',
                type: 'date',
                inputClass: 'validate[required]'
            },
            accessType: {
                title: 'AccessType',
                options: {'Basic': 'Basic', 'Full': 'Full', 'Pro': 'Pro'},
                inputClass: 'validate[required]'
            },
            clan: {
                title: 'Is clan?',
                type: 'checkbox',
                values: {'false': 'User', 'true': 'Clan'},
                defaultValue: true
            },
            comments: {
                title: 'Comments',
                defaultValue: ''
            }
        },
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
    });

    personalContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Users',
        actions: {
            listAction: '/crud/accessList/users',
            createAction: '/crud/accessList/create',
            updateAction: '/crud/accessList/update',
            deleteAction: '/crud/accessList/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            name: {
                title: 'User Name',
                inputClass: 'validate[required]'
            },
            fromDate: {
                title: 'Date begin',
                type: 'date'
            },
            dueDate: {
                title: 'Date end',
                type: 'date',
                inputClass: 'validate[required]'
            },
            accessType: {
                title: 'AccessType',
                options: {'Basic': 'Basic', 'Full': 'Full', 'Pro': 'Pro'},
                inputClass: 'validate[required]'
            },
            clan: {
                title: 'Is clan?',
                type: 'checkbox',
                values: {'false': 'User', 'true': 'Clan'},
                defaultValue: false
            },
            comments: {
                title: 'Comments',
                defaultValue: ''
            }
        },
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
    });

    blackListContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Black list',
        actions: {
            listAction: '/crud/blackList/read',
            createAction: '/crud/blackList/create',
            updateAction: '/crud/blackList/update',
            deleteAction: '/crud/blackList/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            nickOrClanName: {
                title: 'Name',
                inputClass: 'validate[required]'
            },
            reason: {
                title: 'Reason',
                inputClass: 'validate[required]'
            },
            clan: {
                title: 'Is clan?',
                type: 'checkbox',
                values: {'false': 'User', 'true': 'Clan'},
                defaultValue: true
            }
        },
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
    });

    versionContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Versions',
        actions: {
            listAction: '/crud/clientVersion/read',
            createAction: '/crud/clientVersion/create',
            updateAction: '/crud/clientVersion/update',
            deleteAction: '/crud/clientVersion/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            version: {
                title: 'Version',
                inputClass: 'validate[required]'
            },
            updatePolicy: {
                title: 'UpdatePolicy',
                options: {'Required': 'Required', 'Optional': 'Optional'},
                inputClass: 'validate[required]',
                defaultValue: 'Optional'
            },
            betta: {
                title: 'Is betta version?',
                type: 'checkbox',
                values: {'false': 'Release version', 'true': 'Betta version'},
                defaultValue: false
            },
            banned: {
                title: 'Is banned?',
                type: 'checkbox',
                values: {'false': 'Active', 'true': 'Banned'},
                defaultValue: false
            },
            releaseNotes: {
                title: 'Release notes',
                inputClass: 'validate[required]'
            },
            link: {
                title: 'Link',
                inputClass: 'validate[required]'
            }
        },
        //Initialize validation logic when a form is created
        formCreated: function (event, data) {
            data.form.validationEngine();
        },
        //Validate form when it is being submitted
        formSubmitting: function (event, data) {
            return data.form.validationEngine('validate');
        },
        //Dispose validation logic when form is closed
        formClosed: function (event, data) {
            data.form.validationEngine('hide');
            data.form.validationEngine('detach');
        }
    });

    logsContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Logs',
        actions: {
            listAction: '/crud/activateRequest/read'
            //createAction: '/crud/clientVersion/create',
            //updateAction: '/crud/clientVersion/update',
            //deleteAction: '/crud/clientVersion/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            date: {
                title: 'Log in time'
            },
            nickName: {
                title: 'Nick'
            },
            clanName: {
                title: 'Clan name'
            },
            ipAddress: {
                title: 'IP address'
            },
            macAddress: {
                title: 'MAC address'
            },
            clientVersion: {
                title: 'Version'
            },
            activated: {
                title: 'Is activated?'
            },
            isBetta: {
                title: 'Is Betta?'
            },
            accessType: {
                title: 'Access type'
            }
        }
    });

    for (var i = 0; i < containers.length; i++) {
        containers[i].jtable('load');
    }

});