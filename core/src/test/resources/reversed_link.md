Go to (this website)[http://www.example.com] {NoReversedLinksRule}

However, this shouldn't trigger inside code blocks:

    myObj.getFiles("test")[0]

Nor inline code: `myobj.getFiles("test")[1]`

This also shouldn't trigger \[INSERT EMAIL HERE\].
