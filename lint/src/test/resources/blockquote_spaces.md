Some text

> Hello world
>  Foo {NoMultipleSpaceBlockquoteRule}
>  Bar {NoMultipleSpaceBlockquoteRule}

This tests other things embedded in the blockquote:

> *Hello world*
>  *foo* {NoMultipleSpaceBlockquoteRule}
>  **bar** {NoMultipleSpaceBlockquoteRule}
>   "Baz" {NoMultipleSpaceBlockquoteRule}
>   `qux` {NoMultipleSpaceBlockquoteRule}
> *foo* more text
> **bar** more text
> 'baz' more text
> `qux` more text

Test the first line being indented too much:

>  Foo {NoMultipleSpaceBlockquoteRule}
>  Bar {NoMultipleSpaceBlockquoteRule}
> Baz
