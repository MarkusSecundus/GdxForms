package com.markussecundus.formsgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.UberDrawable;
import com.markussecundus.forms.elements.impl.BasicLabel;
import com.markussecundus.forms.elements.impl.ImageIcon;
import com.markussecundus.forms.elements.impl.layouts.BasicGridLayout;
import com.markussecundus.forms.elements.impl.layouts.BasicLinearLayout;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.text.Font;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.vector.Vect2d;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.Property;
import com.markussecundus.forms.wrappers.property.ReadonlyProperty;
import com.markussecundus.forms.wrappers.property.binding.Bindings;
import com.markussecundus.forms.wrappers.property.impl.general.SimpleProperty;
import com.markussecundus.formsgdx.examples.Slider;
import com.markussecundus.formsgdx.graphics.RoundedRectangle;
import com.markussecundus.formsgdx.input.InputManager;
import com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer;
import com.markussecundus.formsgdx.rendering.BasicRenderer;
import com.markussecundus.formsgdx.text.FormsBitmapFont;


/**
 * Ukázkový formulář, který slouží čistě k lehké demonstraci základních možností Formulářové knihovny.
 *
 * @see BasicFormApplication
 * @see com.badlogic.gdx.ApplicationAdapter
 *
 * @author MarkusSecundus
 * */
public class FormsGdxExample extends BasicFormApplication {


	/**
	 * Alias pro jednoduchý {@link BasicLinearLayout} parametrizovaný pro použití nad LibGDX
	 * a s logikou pro zpracování uživatelského vstupu.
	 *
	 * @see com.markussecundus.forms.elements.UberDrawable
	 * @see BasicLinearLayout
	 * @see IListeneredUniversalConsumer
	 * */
	static class Ly extends BasicLinearLayout<BasicRenderer, Vect2f, Float> implements IListeneredUniversalConsumer.ForLayout {
		public Ly(Vect2f dims){ super(dims, Vect2f.getUtility()); }
	}

	/**
	 * Alias pro jednoduchou obrázkovou ikonu vykreslující obecný grafický objekt a s přidanou logikou pro poslouchání uživatelského vstupu.
	 *
	 * @see DrawableElem
	 * @see ImageIcon
	 * @see IListeneredUniversalConsumer
	 *
	 * */
	static class It extends ImageIcon.Plain<BasicRenderer, Vect2f, Float> implements IListeneredUniversalConsumer {
		public It(GraphicalPrimitive<BasicRenderer, Vect2f, Float> img){
			super(img);
		}
	}


	/**
	 * Alias pro jednoduchý {@link BasicGridLayout} parametrizovaný pro použití nad LibGDX
	 * a s logikou pro zpracování uživatelského vstupu.
	 *
	 * @see com.markussecundus.forms.elements.UberDrawable
	 * @see BasicGridLayout
	 * @see IListeneredUniversalConsumer
	 * */
	static class GrLy extends BasicGridLayout<BasicRenderer, Vect2f, Float> implements IListeneredUniversalConsumer.ForLayout{
		public GrLy(VectUtil<Vect2f, Float> posUtil) {
			super(posUtil);
		}
	}

	/**
	 * Alias pro {@link BasicLabel} parametrizovaný pro použití nad LibGDX
	 * a s logikou pro zpracování uživatelského vstupu.
	 *
	 * @see com.markussecundus.forms.elements.UberDrawable
	 * @see BasicLabel
	 * @see IListeneredUniversalConsumer
	 * */
	static class Lbl extends BasicLabel<BasicRenderer, Vect2f, Float> implements IListeneredUniversalConsumer{
		public Lbl(){ super(Vect2f.getUtility()); }
	}


	@Override
	public DrawableElem<BasicRenderer, Vect2f> createForm() {
		BackgroundColor = Color.FOREST;

		DrawableElem<BasicRenderer, Vect2f> ret =  createFormV2();

		return ret;
	}

	/**
	 * Druhý ukázkový formulář.
	 * <p>Náhodná změť prvků sdružených do grid-layoutu.
	 * <p>Obsahuje ukázku bindování properties.
	 * */
	public DrawableElem<BasicRenderer, Vect2f> createFormV2(){
		GrLy ly = new GrLy(Vect2f.getUtility()){{
			setIgnoreTooShort(true);
			setPrefSize(Vect2f.make(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
			setDefaultCellSize(Vect2f.make(150,150));
			setDefaultCellAllignment(0d, 1d);
			//setCollumnSize(0, 2, 300f);
			//setCollumnSize(1, 2, 10f);
			//setCollumnSize(0, 3, 300f);
		}};

		It i = new It(BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(200,200), 0.6f, Vect2f.make(30,30), Color.ORANGE, Color.BROWN)));
		It i1 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(50,200), 0.6f, Vect2f.make(30,30), Color.CYAN, Color.BROWN)));
		It i2 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(100,100), 0.9f, Vect2f.make(30,30), Color.WHITE, Color.GOLD)));


		Style style = new Style() {{
			innerColor = Color.WHITE;
			outerColor = Color.BLACK;
			transitionColor = Color.GOLDENROD;
			edgeRoundness = 0.5f;
			borderRatio = 0.15f;
			borderThickness = Vect2f.make(10,10);
		}};

		//vytvoříme několik Sliderů, délka je přestřelená, jelikož layout si je zarovná
		Slider.Basic slider = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(50,100));
		Slider.Basic slider2 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));
		Slider.Basic slider3 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));
		Slider.Basic slider4 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));

		//nabindujeme vzájemně hodnotu 1. a 2. slideru, takže když se 1 z nich změní, změní se i druhá
		Bindings.bindBidirectional(slider.value(), slider2.value());
		//nabindujeme hodnotu 4. slideru na aritmetický průměr hodnot 2. a 3. slideru
		Bindings.bind(slider4.value(),(a,b)->(a+b)*0.5f, slider2.value(), slider3.value());

		ly.addDrawableChild(i, 0,0);
		ly.addDrawableChild(i1, 1,1);
		ly.addDrawableChild(i2, 1,0);
		ly.addDrawableChild(slider, 2,3);
		ly.addDrawableChild(slider2, 2,2);
		ly.addDrawableChild(slider3, 0,1);
		ly.addDrawableChild(slider4, 3,1);

		Font<BasicRenderer, Vect2f, Float> font = FormsBitmapFont.make_Vect2f(new BitmapFont(), Color.BLUE, 2.3f);

		class Wrap {float deltaSum = 0;int frameCount = 0;float elapsedTime=0;}
		Wrap wr = new Wrap();

		Lbl lbl = new Lbl(){{
			setFont(font);
			setPrefSize(Vect2f.make(1000, 300));
			setAllignment(Vect2d.make(1d,0.5d));
			}

			@Override public void update(float delta, int frameId) {
				super.update(delta, frameId);
				//setText(String.format("FPS: %.2f", 1f/delta));
				setText(String.format("fps: %.2f\nFPS: %.0f\navg: %.2f\n", 1f/delta, 1/((wr.deltaSum += delta)/++wr.frameCount), 1/((wr.elapsedTime+=delta)/frameId)));
				if (wr.deltaSum >0.2f){
					wr.frameCount = 0;
					wr.deltaSum =0;
				}
			}

			@Override
			public boolean draw(BasicRenderer renderer, Vect2f position) {
				ShapeRenderer sh = renderer.getShape();
				Vect2f size = getSize();
				sh.setColor(Color.YELLOW);
				sh.rect(position.x, position.y, size.x, size.y);
				return super.draw(renderer, position);
			}
		};

		ly.addDrawableChild(lbl, 2, 1);

		//ly.getChildren().add((delta, frameId) -> lbl.setText(String.format("FPS: %.2f", 1f/delta)));

		//vytvoříme manager na zpracování vstupu od uživatele
		InputManager.AsInputProcessor man = new InputManager.AsInputProcessor();
		man.getTouchConsumers().add(ly); //zaregistrujeme do něj kořenový layout
		Gdx.input.setInputProcessor(man); //zaregistujeme ho, aby dostával vstup ze systému

		return ly;
	}









	/**
	 * Starý ukázkový formulář.
	 * Používá systém vnořených lineárních layoutů.
	 * */
	public DrawableElem<BasicRenderer, Vect2f> createFormV1() {

		//vytvoříme kořenový layout
		Ly ly = new Ly(Vect2f.make(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())) {{
				setIgnoreTooShort(true);    //je nám jedno, když z něj prvky vytečou
				setDimension(1);    //prvky v něm budou uspořádané podle osy y
				setAlignment(1, 0f); //v souřadnici y budou zarovnané na střed
				setAlignment(0, 0.25f); //v souřadnici x budou jeho prvky zarovnané na 1/4 délky zleva
				setInnerPadding(10f);  //nastavíme úhledné odsazení
				setOuterPadding(Vect2f.make(10,10));
		}};

		//vytvoříme nějaké obrázkové ikony - 'RoundedRectangle' bere jako renderer 'SpriteBatch' - je třeba je obalit do převodníku
		It i = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(200,200), 0.6f, Vect2f.make(30,30), Color.ORANGE, Color.BROWN)));
		It i1 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(50,200), 0.6f, Vect2f.make(30,30), Color.CYAN, Color.BROWN)));
		It i2 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(100,100), 0.9f, Vect2f.make(30,30), Color.WHITE, Color.GOLD)));

		//vytvoříme Styl, abychom další prvky GUI mohli inicializovat pohodlněji
		Style style = new Style() {{
			innerColor = Color.WHITE;
			outerColor = Color.BLACK;
			transitionColor = Color.GOLDENROD;
			edgeRoundness = 0.5f;
			borderRatio = 0.15f;
			borderThickness = Vect2f.make(10,10);
		}};

		//vytvoříme dva Slidery, délka je přestřelená, jelikož layout si je zarovná
		Slider.Basic slider = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(50,100));
		Slider.Basic slider2 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));

		//délka ikony i se bude měnit podle hodnoty na 1. posuvníku
		slider.value().getSetterListeners().add(e->i.setPrefSize(Vect2f.make(ly.getSize().x*e.newVal().get(), i.getPrefSize().y).withFloor(style.borderThickness.scl(2))));


		//přidáme do layoutu nevykreslitelného potomka, který akorát každý snímek provede svou funkci update - každý snímek povyroste ikona 'i1' o hodnotu 'rychlostRustu'
		Wrapper<Vect2f> rychlostRustu = Wrapper.make(Vect2f.make(0.1f, 0.05f));
		ly.getChildren().add((d, num)->i1.setPrefSize(i1.getPrefSize().add(rychlostRustu.get()).withFloor(Vect2f.ZERO).withCeiling(ly.getSize())));
		ly.getOnClickedListener().add(e->Gdx.app.log("ttt", "clicked!"));

		//hodnota 'rychlostRustu' bude záviset na hodnotách posuvníků
		slider2.value().getSetterListeners().add(e->rychlostRustu.set(rychlostRustu.get().withX(e.newVal().get()*2f-1f)));
		slider.value().getSetterListeners().add(e->rychlostRustu.set(rychlostRustu.get().withY(e.newVal().get()*2f-1f)));


		//vytvoříme další slider
		Slider.Basic slider3 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));

		//hodnota slideru bude určovat zarovnání kořenového layoutu
		slider3.value().getSetterListeners().add(e->ly.setAlignment(0, e.newVal().get()));
		

		//přidáme všechny prvky do kořenového layoutu
		ly.getDrawableChildren().add(slider3);
		ly.getDrawableChildren().add(i);
		ly.getDrawableChildren().add(slider);
		ly.getDrawableChildren().add(i1);
		ly.getDrawableChildren().add(slider2);
		ly.getDrawableChildren().add(i2);


		//vytvoříme další layout, x si můžeme dovolit přestřelit
		Ly ll = new Ly(Vect2f.make(10000, 500)) {{
			setIgnoreTooShort(true);
			setInnerPadding(50f);
		}};

		//a ještě jeden
		Ly ll2 = new Ly(Vect2f.make(ly.getSize().x, 500)) {{
			setIgnoreTooShort(true);
			setInnerPadding(40f);				//nastavíme pěkné odsazení a zarovnání
			setAlignment(0,1f);
		}};

		//vytvoříme slider a nastavíme, že hodnota na něm udává zarovnání vnitřního layoutu 'll'
		Slider.Basic sl1 = new Slider.Basic(style, Vect2f.make(300,80), Vect2f.make(50,100));
		sl1.value().getSetterListeners().add(e->ll.setAlignment(1, e.newVal().get()));

		//vytvoříme další slider, od jeho hodnoty se bude odvíjet kulatost jeho čudlíku
		Slider.Basic sl2 = new Slider.Basic(style, Vect2f.make(300,80), Vect2f.make(100,100));
		sl2.value().getSetterListeners().add(e->sl2.cudlik.base.roundness().set(e.newVal().get()));

		//přidáme prvky do vnitřního layoutu
		ll2.getDrawableChildren().add(sl1);
		ll2.getDrawableChildren().add( sl2);

		//nastavíme layout přesně na velikost, kterou mají dohromady všechny jeho prvky
		ll2.setPrefSize(ll2.computeRealSize());

		//ještě vytvoříme několik ikon a přidáme je do 2. vnitřního layoutu
		Style style2 = new Style(){{
			innerColor = Color.CYAN;
			outerColor = Color.BLACK;
			transitionColor = Color.BROWN;
			edgeRoundness = 0.9f;
			borderRatio = 0.1f;
			borderThickness = Vect2f.make(10,10);
		}};
		ll.getDrawableChildren().add(new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(250,254), style2))));
		It i4 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(250,134), style2)));//do vnitřního layoutu přidáme nový prvek
		ll.getDrawableChildren().add(i4);
		It i5 = new It(BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(150,100), style2)));//přidáme další prvek

		ll.getDrawableChildren().add(i5);
		ll.applySizeChanges();
		ll.setPrefSize(ll.computeRealSize()); //nastavíme layout přesně na velikost, kterou mají jeho prvky dohromady

		//oba vnitřní layouty přidáme do kořenového layoutu
		ly.getDrawableChildren().add(ll);
		ly.getDrawableChildren().add(ll2);

		//ly.setPrefSize(ly.computeRealSize());

		//vytvoříme manager na zpracování vstupu od uživatele
		InputManager.AsInputProcessor man = new InputManager.AsInputProcessor();
		man.getTouchConsumers().add(ly); //zaregistrujeme do něj kořenový layout
		Gdx.input.setInputProcessor(man); //zaregistujeme ho, aby dostával vstup ze systému

		return ly; //vrátíme kořenový layout
	}

}
