Normal list

* Foo
* Bar
* Baz

List with incorrect spacing

*  Foo {ListMarkerSpaceRule}
*  Bar {ListMarkerSpaceRule}
*   Baz {ListMarkerSpaceRule}

List with children:

* Foo {ListMarkerSpaceRule}
    * Bar {ListMarkerSpaceRule}
        * Baz

List with children and correct spacing:

*   Foo
    *   Bar
        * Baz (This sublist has no children)

List with Multiple paragraphs and correct spacing

*   Foo

    Here is the second paragraph

*   All items in the list need the same indent

List with multiple paragraphs and incorrect spacing

*  Foo {ListMarkerSpaceRule}

   Here is the second paragraph

*    Bar {ListMarkerSpaceRule}

List with code blocks:

*   Foo

        Here is some code

*   Bar

Ordered lists:

1. Foo
1. Bar
1. Baz

And with incorrect spacing:

1.  Foo {ListMarkerSpaceRule}
1.  Bar {ListMarkerSpaceRule}
1.  Baz {ListMarkerSpaceRule}

Ordered lists with children:

1. Foo {ListMarkerSpaceRule}
    * Hi
1. Bar {ListMarkerSpaceRule}
1. Baz {ListMarkerSpaceRule}

Ordered lists with children (correct spacing), and with something other than
the first item determining that the entire list has children:

1.  Foo
1.  Bar
    * Hi
1.  Baz
