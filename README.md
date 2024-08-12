A. Postman requests for testing newly added transfer amount feature.

1. Create accounts
POST request with URL : http://localhost:18080/v1/accounts/
{
	"accountId" : "abc",
	"balance" : "2000"
}

{
	"accountId" : "def",
	"balance" : "4000"
}

2. transfer amount from one account to other.
POST request with URL : http://localhost:18080/v1/accounts/transfer
{
    "accountFromId" : "abc",
    "accountToId" : "def",
    "amount" : "1000"
}

3. get account details.
GET request with URL : http://localhost:18080/v1/accounts/abc
sample response:
{
    "accountId": "abc",
    "balance": 1000
}

4. transfer amount with invalid account number.
POST request with URL : http://localhost:18080/v1/accounts/transfer
{
    "accountFromId" : "abc1",
    "accountToId" : "def",
    "amount" : "1000"
}
Sample response : Account id abc1 does not exist!

5. Insufficient amount.
POST request with URL : http://localhost:18080/v1/accounts/transfer
{
    "accountFromId" : "abc",
    "accountToId" : "def",
    "amount" : "10000"
}
Sample response : Insufficient Amount in Account id abc!


B. Additional action to be considered for better security can be implemented with integration of JWT token for request authentication and authorization.