$(function () {
    var body = $('body'),
        WITHDRAW_FUNDS = 'Withdraw funds',   //
        ADD_FUNDS = 'Add funds',             // Constants
        TRANSFER_FUNDS = 'Transfer funds';   //

    var users = $.ajax({
        url: '/users',
        contentType: 'application/json',
        type: "get",
        complete: function (response) {
            if (response.status == 404) {
                $.growl.notice({message: "No users found"});
            }
        },
        success: function (response) {
            for (var i = 0; i < response.length; i++) {
                var fullName = response[i].firstName + ' ' + response[i].lastName;
                response[i].fullName = '<a class="accountsLink">' + fullName + '</a>';
            }
        }
    });

    body.on('click', 'a.accountsLink', function () {

        var userId = $(this).closest('tr').find('td:eq(0)').text(),
            userName = $(this).text(),
            userEmail = $(this).closest('tr').find('td:eq(2)').text();

        $('#userInfoAppender').html('<h3> List of accounts of user: ' +
            userName + ' (ID: <div id="divUserId">' + userId + '</div>, Email: ' + userEmail + ')</h3>');
        var accountsString = '';
        console.log('/accounts/user/' + userId);
        $.getJSON('/accounts/user/' + userId, function (accountsData) {
            $.each(accountsData, function (index, data) {
                accountsString +=
                    '<li class="list-group-item"><h4>Account number: <div class="liAccountNumber">' +
                    data.accountNumber + '</div></h4>' +
                    '<h4>Current funds: <div class="liAccountCurrency">' + data.currency + '</div></h4>' +
                    '<button class="btn btn-primary btnWithdraw">Withdraw funds</button>' +
                    '<button class="btn btn-primary btnAdd">Add funds</button>' +
                    '<button class="btn btn-primary btnTransfer">Transfer funds</button>' +
                    '<button class="btn btn-primary btnDelete">Delete account</button>' + '</li>';
            });
            $('#listOfUsers').append(accountsString);
        });
        //todo implement create account button
        $('#createAccountAppender').html('<button class="btn btn-primary btnCreate">Create new account</button>');
        $('#mainPageAppender').html('<a href=""><p>Back to main page</p></a>');
    });

    var accountsModal = $('#accounts-modal'),
        accountsEditor = $('#accounts-editor'),
        accountsEditorTitle = $('#accounts-editor-title');

    body.on('click', 'button.btnWithdraw', function () {
        // accountsEditor.find('#oldCapacity').val($(this).closest('li').find('div:eq(1)').text());
        accountsEditorTitle.text(WITHDRAW_FUNDS);
        $('#hiddenNumber').hide();
        setAndShowModal($(this))
    });

    body.on('click', 'button.btnAdd', function () {
        accountsEditorTitle.text(ADD_FUNDS);
        $('#hiddenNumber').hide();
        setAndShowModal($(this))
    });

    body.on('click', 'button.btnTransfer', function () {
        accountsEditorTitle.text(TRANSFER_FUNDS);
        $('#hiddenNumber').show();
        setAndShowModal($(this));
    });

    body.on('click', 'button.btnCreate', function () {
        $.ajax({
            url: '/accounts',
            contentType: 'application/json',
            type: "post",
            data: JSON.stringify({
                id: $('#divUserId').text()
            }),
            success: function (response) {
                var ohMyGodWhatAString =
                    '<li class="list-group-item"><h4>Account number: <div class="liAccountNumber">' +
                    response.accountNumber + '</div></h4>' +
                    '<h4>Current funds: <div class="liAccountCurrency">' + response.currency + '</div></h4>' +
                    '<button class="btn btn-primary btnWithdraw">Withdraw funds</button>' +
                    '<button class="btn btn-primary btnAdd">Add funds</button>' +
                    '<button class="btn btn-primary btnTransfer">Transfer funds</button>' +
                    '<button class="btn btn-primary btnDelete">Delete account</button>' + '</li>';
                $('#listOfUsers').append(ohMyGodWhatAString);
                $.growl.notice({message: 'Success'});
            }
        });
    });

    function setAndShowModal(element) {
        accountsEditor[0].reset();
        accountsEditor.find('#accountNumber').val(element.closest('li').find('div:eq(0)').text());
        accountsModal.modal('show');
    }

    body.on('click', 'button.btnDelete', function () {
        if (confirm('Are you sure you want to delete the account?')) {
            var listItem = $(this).closest('li'),
                accountId = listItem.find('div:eq(0)').text();
            console.log(accountId);
            $.ajax({
                url: '/accounts/' + accountId,
                contentType: 'application/json',
                type: "delete",
                data: JSON.stringify({
                    id: accountId
                }),
                success: function () {
                    listItem.remove();
                }
            });
        }
    });

    accountsEditor.on('submit', function (e) {
        e.preventDefault();
        var lis = document.getElementsByClassName('liAccountNumber'),
            accountNumberFromModal = accountsEditor.find('#accountNumber').val().trim(),
            amountFromModal = accountsEditor.find('#amount').val().trim(),
            accountToTransferFromModal = accountsEditor.find('#accountToTransfer').val().trim();
        switch (accountsEditorTitle.text()) {
            case WITHDRAW_FUNDS :
                $.ajax({
                    url: '/transfer/withdraw',
                    contentType: 'application/json',
                    type: "put",
                    data: JSON.stringify({
                        fromAccount: accountNumberFromModal,
                        amount: amountFromModal
                    }),
                    complete: function (response) {
                        accountsModal.modal('hide');
                        if (response.status == 400)
                            $.growl.notice({message: response.responseText});
                    },
                    success: function (response) {
                        accountsModal.modal('hide');
                        [].forEach.call(lis, function (el) {
                            if ($(el).text() == response.fromAccount) {
                                $(el).closest('li').find('div:eq(1)').html(response.senderResultCurrency);
                            }
                        });
                        $.growl.notice({message: 'Success'});
                    }
                });
                break;
            case ADD_FUNDS :
                $.ajax({
                    url: '/transfer/add/',
                    contentType: 'application/json',
                    type: "put",
                    data: JSON.stringify({
                        toAccount: accountNumberFromModal,
                        amount: amountFromModal
                    }),
                    success: function (response) {
                        accountsModal.modal('hide');
                        [].forEach.call(lis, function (el) {
                            if ($(el).text() == response.fromAccount) {
                                $(el).closest('li').find('div:eq(1)').html(response.senderResultCurrency);
                            }
                        });
                        $.growl.notice({message: 'Success'});
                    }
                });
                break;
            case TRANSFER_FUNDS :
                $.ajax({
                    url: '/transfer',
                    contentType: 'application/json',
                    type: "put",
                    data: JSON.stringify({
                        fromAccount: accountNumberFromModal,
                        toAccount: accountToTransferFromModal,
                        amount: amountFromModal
                    }),
                    complete: function (response) {
                        accountsModal.modal('hide');
                        if (response.status == 400)
                            $.growl.notice({message: response.responseText});
                        else if (response.status == 404)
                            $.growl.notice({message: response.responseText});
                    },
                    success: function (response) {
                        accountsModal.modal('hide');
                        [].forEach.call(lis, function (el) {
                            if ($(el).text() == response.fromAccount) {
                                $(el).closest('li').find('div:eq(1)').html(response.senderResultCurrency);
                            }
                            if ($(el).text() == response.toAccount) {
                                $(el).closest('li').find('div:eq(1)').html(response.recipientResultCurrency);
                            }
                        });
                        $.growl.notice({message: 'Success'});
                    }
                });
                break;
        }
    });

    var usersModal = $('#editor-modal'),
        usersEditor = $('#editor'),
        usersEditorTitle = $('#editor-title'),
        ft = FooTable.init('#usersTable', {
            columns: [
                {"name": "id", "title": "ID"},
                {"name": "fullName", "title": "Full Name"},
                {"name": "email", "title": "Email"}
            ],
            rows: users,
            editing: {
                enabled: true,
                addRow: function () {
                    usersModal.removeData('row');
                    usersEditor[0].reset();
                    usersEditorTitle.text('Add user');
                    usersModal.modal('show');
                },
                editRow: function (row) {
                    var values = row.val();
                    console.log(values.fullName);
                    var nameWithoutTag = $(values.fullName);
                    nameWithoutTag.find('a').remove();
                    nameWithoutTag = nameWithoutTag.html();
                    usersEditor.find('#id').val(values.id);
                    usersEditor.find('#firstName').val(nameWithoutTag.split(" ")[0]);
                    usersEditor.find('#lastName').val(nameWithoutTag.split(" ")[1]);
                    usersEditor.find('#email').val(values.email);

                    usersModal.data('row', row);
                    usersEditorTitle.text('Edit user');
                    usersModal.modal('show');
                },
                deleteRow: function (row) {
                    if (confirm('Are you sure you want to delete the row?')) {
                        var userId = row.val().id;
                        $.ajax({
                            url: '/users/' + userId,
                            contentType: 'application/json',
                            type: "delete",
                            data: JSON.stringify({
                                id: userId
                            }),
                            success: function () {
                                row.delete();
                                $.growl.notice({message: 'Success'});
                            }
                        });
                    }
                }
            }
        });

    usersEditor.on('submit', function (e) {
        if (this.checkValidity && !this.checkValidity())
            return;

        e.preventDefault();
        var row = usersModal.data('row'),

            idFromModal = usersEditor.find('#id').val().trim(),
            firstNameFromModal = usersEditor.find('#firstName').val().trim(),
            lastNameFromModal = usersEditor.find('#lastName').val().trim(),
            emailFromModal = usersEditor.find('#email').val().trim();

        if (row instanceof FooTable.Row) {
            $.ajax({
                url: '/users/' + idFromModal,
                contentType: 'application/json',
                type: "put",
                data: JSON.stringify({
                    id: idFromModal,
                    firstName: firstNameFromModal,
                    lastName: lastNameFromModal,
                    email: emailFromModal,
                    accountSet: null
                }),
                success: function () {
                    var edits = {
                        id: idFromModal,
                        fullName: '<a class="accountsLink">' + firstNameFromModal + ' ' + lastNameFromModal + '</a>',
                        email: emailFromModal
                    };
                    row.val(edits);
                    $.growl.notice({message: 'Success'});
                }
            });
        } else {
            $.ajax({
                url: '/users',
                contentType: 'application/json',
                type: "post",
                data: JSON.stringify({
                    firstName: firstNameFromModal,
                    lastName: lastNameFromModal,
                    email: emailFromModal,
                    accountSet: null
                }),
                success: function (response) {
                    var adds = {
                        id: response.id,
                        fullName: '<a class="accountsLink">' + firstNameFromModal + ' ' + lastNameFromModal + '</a>',
                        email: emailFromModal
                    };
                    ft.rows.add(adds);
                    $.growl.notice({message: 'Success'});
                }
            });
        }
        usersModal.modal('hide');
    });
})
;
