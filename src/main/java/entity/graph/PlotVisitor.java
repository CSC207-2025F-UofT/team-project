package entity.graph;

// 2d or 3d without instanceof chains, same idea as NodeVisitor
public interface PlotVisitor<T> {
    T visitCurve(CurvePlot plot);

    T visitSurface(SurfacePlot plot);
}
