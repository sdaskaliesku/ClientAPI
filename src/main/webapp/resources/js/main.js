/**
 * @author sdaskaliesku
 */
$(document).ready(function () {

    var clanContainer = $('#clanContainer');
    var personalContainer = $('#personalContainer');
    var blackListContainer = $('#blackListContainer');
    var versionContainer = $('#versionContainer');
    var logsContainer = $('#logsContainer');
    var usersContainer = $('#usersContainer');
    var cryptoKeyContainer = $('#cryptoKeyContainer');
    var freeFunctionsContainer = $('#freeFunctionsContainer');

    var containers = [clanContainer, personalContainer, blackListContainer, versionContainer, logsContainer, usersContainer, cryptoKeyContainer, freeFunctionsContainer];

    //Initialize validation logic when a form is created
    function formCreated(event, data) {
        data.form.validationEngine('hide');
        data.form.validationEngine('detach');
    }

    //Validate form when it is being submitted
    function formSubmitting(event, data) {
        return data.form.validationEngine('validate');
    }

    //Dispose validation logic when form is closed
    function formClosed(event, data) {
        data.form.validationEngine('hide');
        data.form.validationEngine('detach');
    }

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

        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
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
        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
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
        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
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
            date: {
                title: 'Date',
                inputClass: 'validate[required]',
                type: 'date'
            },
            updatePolicy: {
                title: 'UpdatePolicy',
                options: {'Required': 'Required', 'Optional': 'Optional'},
                inputClass: 'validate[required]',
                defaultValue: 'Optional'
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
        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
    });

    logsContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Logs',
        actions: {
            listAction: '/crud/activateRequest/read',
            //createAction: '/crud/activateRequest/create',
            //updateAction: '/crud/activateRequest/update',
            deleteAction: '/crud/activateRequest/delete'
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
            accessType: {
                title: 'Access type'
            }
        }
    });

    usersContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Users',
        actions: {
            listAction: '/crud/account/read',
            // createAction: '/crud/account/create',
            updateAction: '/crud/account/update',
            deleteAction: '/crud/account/delete'
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
            role: {
                title: 'Role',
                options: {'ROLE_ADMIN': 'ROLE_ADMIN', 'ROLE_USER': 'ROLE_USER', 'ROLE_NOTHING': 'ROLE_NOTHING'},
                inputClass: 'validate[required]'
            }
        },
        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
    });

    cryptoKeyContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'CryptoKey',
        actions: {
            listAction: '/crud/cryptoKey/read',
            createAction: '/crud/cryptoKey/create',
            updateAction: '/crud/cryptoKey/update',
            deleteAction: '/crud/cryptoKey/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            date: {
                title: 'Date',
                type: 'date',
                inputClass: 'validate[required]'
            },
            cryptoKey: {
                title: 'Key',
                inputClass: 'validate[required]'
            }
        },
        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
    });

    freeFunctionsContainer.jtable({
        jqueryuiTheme: true,
        ajaxSettings: {type: 'GET'},
        title: 'Free functions',
        actions: {
            listAction: '/crud/freeFunctions/read',
            createAction: '/crud/freeFunctions/create',
            updateAction: '/crud/freeFunctions/update',
            deleteAction: '/crud/freeFunctions/delete'
        },
        fields: {
            id: {
                title: '#',
                key: true
            },
            name: {
                title: 'Name',
                inputClass: 'validate[required]'
            }
        },
        formCreated: formCreated,
        formSubmitting: formSubmitting,
        formClosed: formClosed
    });

    for (var i = 0; i < containers.length; i++) {
        containers[i].jtable('load');
    }

});