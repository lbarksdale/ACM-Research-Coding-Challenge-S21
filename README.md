# ACM Research Coding Challenge (Spring 2021)

##Process to Find Solution
I followed a fairly straightforward path to get my solution. I began by
doing preliminary research on circular genome maps, which actually led
to my discovery of a helpful Java library, CGView. As I had intended to
use Java from the start (I am most familiar with Java), I decided to
utilize CGView for this project. CGView generates a circular genome map
given the proper input, so most of what I did was parse the genbank file
given to us to give CGView the proper input.

Information about the CGView library can be found at
https://paulstothard.github.io/cgview/index.html

##Shortcomings of the Program
While my program adequately solves the issue at hand, there are several
places where it could be improved. First, it poorly handles large numbers
of overlapping labels. At present, it places each new label on a new ring,
meaning that even when labels to not overlap, the circular map grows in
size. This can hamper readability of larger genome maps.
Furthermore, labels are randomly assigned a color to help differentiate
between labels. This was done because when genes overlap, it could be
difficult to determine which label was pointing to each gene. While this
feature is helpful, it can result in gene and label colors that are
hard to read, or excessively similar.

If I were to improve this program, I would likely focus on making genes
and gene labels be able to overlap. With this done, I could eliminate the
need to have each new label on a separate ring, severely reducing the size
of the genome map. I would also standardize the colors, so that they cycle
through a consistent color scheme that ensures good readability throughout.