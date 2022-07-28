package elfak.mosis.freelencelive.data

data class Comment(var issuedBy: User,
                   var issuedFor: User,
                   var text: String
                   )
