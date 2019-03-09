Some text

> a quote
> same quote

> blank line above me


> two blank lines above me
 
> space above me

* List with embedded blockquote

  > Test
  > Test

  > Test

* Item 2

  > Test. The blank line below should _not_ trigger MD028 as one blockquote is
  > inside the list, and the other is outside it.

> Test

Expected errors:

{NoBlanksBlockquoteRule:5} {NoBlanksBlockquoteRule:8}
{NoBlanksBlockquoteRule:10} {NoBlanksBlockquoteRule:17}
{NoTrailingSpacesRule:10} (trailing space is intentional)
{NoMultipleBlanksRule:8} (multiple blank lines are intentional)
