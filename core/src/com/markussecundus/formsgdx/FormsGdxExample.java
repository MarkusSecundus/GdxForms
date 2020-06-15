package com.markussecundus.formsgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.markussecundus.forms.elements.Drawable;
import com.markussecundus.forms.elements.impl.ImageIcon;
import com.markussecundus.forms.elements.impl.layouts.BasicLinearLayout;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.formsgdx.examples.Slider;
import com.markussecundus.formsgdx.graphics.RoundedRectangle;
import com.markussecundus.formsgdx.input.InputManager;
import com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer;
import com.markussecundus.formsgdx.input.interfaces.ListeneredUniversalConsumer;
import com.markussecundus.formsgdx.rendering.BasicRenderer;


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
	static abstract class Ly extends BasicLinearLayout<BasicRenderer, Vect2f, Float> implements ListeneredUniversalConsumer {
		private Ly(Vect2f dims){ super(dims, Vect2f.getUtility()); }

		/**
		 * @param dims preferovaná velikost layoutu
		 *
		 * Factory, přes kterou jedině lze layout vytvořit.
		 * */
		public static Ly make(Vect2f dims) {return new Impl(dims);}

		private static class Impl extends Ly implements IListeneredUniversalConsumer.ForLayout{
			private Impl(Vect2f dims){
				super(dims);
			}

		}
	}

	/**
	 * Alias pro jednoduchou obrázkovou ikonu vykreslující obecný grafický objekt a s přidanou logikou pro poslouchání uživatelského vstupu.
	 *
	 * @see Drawable
	 * @see ImageIcon
	 * @see IListeneredUniversalConsumer
	 *
	 * */
	static abstract class It extends ImageIcon.Plain<BasicRenderer, Vect2f, Float> implements ListeneredUniversalConsumer {
		private It(GraphicalPrimitive<BasicRenderer, Vect2f, Float> img){
			super(img);
		}

		/**
		 * @param img preferovaná velikost layoutu
		 *
		 * Factory, přes kterou jedině lze layout vytvořit.
		 * */
		public static It make( GraphicalPrimitive<BasicRenderer, Vect2f, Float> img){
			return new Impl( img);
		}


		private static class Impl extends It implements IListeneredUniversalConsumer{
			private Impl( GraphicalPrimitive<BasicRenderer, Vect2f, Float> img){super( img);}
		}
	}


	// zdědíme základní chování z ImageIcon; chceme se renderovat přes BasicRenderer a používat Vect2f jako vektor (3. parametr pak je skalár náležící vektorovému typu);
	// RoundedRectangle.SObrubou se renderuje přes ShapeRenderer - je třeba ho zkonvertovat;
	// díky tomu, že si tu přesně pamatujeme, co za typ uvnitř používáme, budeme se moct následně odkazovat
	// na jeho vnitřní prvky, což se hodí (pokud to nepotřebujeme a chceme si ušetřit psaní, je tu typ ImageIcon.Plain)
	class Cudlik extends ImageIcon<BasicRenderer, Vect2f, Float, BasicRenderer.ShapeToBasicDrw<Vect2f, Float,RoundedRectangle.SObrubou> >
				 implements IListeneredUniversalConsumer{  	//k funkcionalitě ImageIcon přidáme zpracování uživatelského vstupu

		static final float ROUNDNESS = 0.5f;		//zakulacenost hran čudlíku, abychom pro ni měli aspoň nějakou hodnotu a nemuseli ji pokaždé předávat
		static final float BORDER_RATIO = 0.3f;		//podle toho určíme tloušťku stěn čudlíku

		//bere rozměry pro čudlík a jeho barvy
		public Cudlik(Vect2f dims, Color bcgCol, Color frgCol, Color middleCol) {

			//zkonstruujeme ImageIcon - vytvoříme a předáme jí její vnitřní grafické primitivum
			super(BasicRenderer.conv(new RoundedRectangle.SObrubou(dims, ROUNDNESS, dims.scl(BORDER_RATIO), frgCol, bcgCol)));

			//při kliknutí na ikonu změníme barvu jejího vnitřku na middleCol
			this.getOnClickedListener().add(e->img.base.getInner().setColor(middleCol));

			//při odkliknutí vrátíme barvu do původního stavu
			this.getOnUnclickedListener().add(e->img.base.getInner().setColor(frgCol));
		}
	}


	//zdědíme funkcionalitu od BasicLinearLayout
	class LinearLayout extends BasicLinearLayout<BasicRenderer, Vect2f, Float>
			implements IListeneredUniversalConsumer.ForLayout{	//přidáme zpracování vstupu - tato varianta přidá i jeho distribuci mezi podprvky

		//Zkonstruujeme vnitřní layout, víc netřeba
		public LinearLayout(Vect2f prefSize, VectUtil<Vect2f, Float> posUtil) {
			super(prefSize, posUtil);
		}
	}


	class Formular extends BasicFormApplication{

		@Override public Drawable<BasicRenderer, Vect2f> createForm() {
			//vytvoříme layout přesně o velikosti obrazovky
			LinearLayout root = new LinearLayout(Vect2f.make(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), Vect2f.getUtility());

			root.setDimension(1); 								//chceme ho mít zarovnaný podle osy y
			root.setAlignment(1, 0.5f);	//zarovnaný na prostředek

			//vytvoříme nějaké dva čudlíky
			Cudlik cudl1 = new Cudlik(Vect2f.make(100, 100), Color.BROWN, Color.WHITE, Color.PINK);
			Cudlik cudl2 = new Cudlik(Vect2f.make(120, 80), Color.CHARTREUSE, Color.FOREST, Color.GOLD);

			// kliknutím na cudl2 se zvětší cudl1
			cudl2.getOnClickedListener().add(e->cudl1.setPrefSize(cudl1.getPrefSize().add(20,30)));

			//vytvoříme další layout, který pak do kořenového vložíme
			LinearLayout horizontal = new LinearLayout(Vect2f.make(500, 200), Vect2f.getUtility());
			horizontal.setDimension(0);             //zarovnaný podle osy x - není potřeba, taková je implicitní hodnota

			//vytvoříme další čudlíky
			Cudlik cudl3 = new Cudlik(Vect2f.make(250, 200), Color.CHARTREUSE, Color.BLUE, Color.RED);
			Cudlik cudl4 = new Cudlik(Vect2f.make(200, 150), Color.SCARLET, Color.CYAN, Color.CORAL);

			horizontal.setInnerPadding(50f);		//nastavíme vnitřní zarovnání v layoutu

			//přidáme prvky do vnořeného layoutu
			horizontal.getChildren().add(cudl3);
			horizontal.getChildren().add(cudl4);

			//všechno přidáme do kořenového layoutu
			root.getChildren().add(cudl1);
			root.getChildren().add(cudl2);
			root.getChildren().add(horizontal);

			//vrátíme kořenový layout
			return root;
		}
	}


	@Override
	public Drawable<BasicRenderer, Vect2f> createForm() {

		BackgroundColor = Color.FOREST;

		//vytvoříme kořenový layout
		Ly ly = Ly.make(Vect2f.make(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		ly.ignoreTooShort.set(true);	//je nám jedno, když z něj prvky vytečou
		ly.setDimension(1);	//prvky v něm budou uspořádané podle osy y
		ly.setAlignment(1, 0.5f); //v souřadnici y budou zarovnané na střed
		ly.setAlignment(0, 0.25f); //v souřadnici x budou jeho prvky zarovnané na 1/4 délky zleva

		//vytvoříme nějaké obrázkové ikony - 'RoundedRectangle' bere jako renderer 'SpriteBatch' - je třeba je obalit do převodníku
		It i = It.make( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(200,200), 0.6f, Vect2f.make(30,30), Color.ORANGE, Color.BROWN)));
		It i1 = It.make( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(50,200), 0.6f, Vect2f.make(30,30), Color.CYAN, Color.BROWN)));
		It i2 = It.make( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(100,100), 0.9f, Vect2f.make(30,30), Color.WHITE, Color.GOLD)));

		//vytvoříme Styl, abychom další prvky GUI mohli inicializovat pohodlněji
		Style style = new Style(Color.WHITE, Color.BLACK,Color.GOLDENROD,0.5f, 0.15f, Vect2f.make(10,10));

		//vytvoříme dva Slidery, délka je přestřelená, jelikož layout si je zarovná
		Slider.Basic slider = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(50,100));
		Slider.Basic slider2 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));

		//délka ikony i se bude měnit podle hodnoty na 1. posuvníku
		slider.value().getSetterListeners().add(e->i.setPrefSize(Vect2f.make(ly.getSize().x*e.newVal.get(), i.getPrefSize().y).withFloor(style.borderThickness.scl(2))));


		//přidáme do layoutu nevykreslitelného potomka, který akorát každý snímek provede svou funkci update - každý snímek povyroste ikona 'i1' o hodnotu 'rychlostRustu'
		Wrapper<Vect2f> rychlostRustu = Wrapper.make(Vect2f.make(0.1f, 0.05f));
		ly.getChildren().add((d, num)->i1.setPrefSize(i1.getPrefSize().add(rychlostRustu.get()).withFloor(Vect2f.ZERO).withCeiling(ly.getSize())));

		//hodnota 'rychlostRustu' bude záviset na hodnotách posuvníků
		slider2.value().getSetterListeners().add(e->rychlostRustu.set(rychlostRustu.get().withX(e.newVal.get()*2f-1f)));
		slider.value().getSetterListeners().add(e->rychlostRustu.set(rychlostRustu.get().withY(e.newVal.get()*2f-1f)));


		//vytvoříme další slider
		Slider.Basic slider3 = new Slider.Basic(style, Vect2f.make(12000,80), Vect2f.make(100,100));

		//hodnota slideru bude určovat zarovnání kořenového layoutu
		slider3.value().getSetterListeners().add(e->ly.setAlignment(0, e.newVal.get()));



		//přidáme všechny prvky do kořenového layoutu
		ly.getDrawableChildren().add(slider3);
		ly.getDrawableChildren().add(i);
		ly.getDrawableChildren().add(slider);
		ly.getDrawableChildren().add(i1);
		ly.getDrawableChildren().add(slider2);
		ly.getDrawableChildren().add(i2);
		ly.getDrawableChildren().add(new Cudlik(Vect2f.make(200,200), Color.BROWN, Color.WHITE, Color.YELLOW));


		//vytvoříme další layout, x si můžeme dovolit přestřelit
		Ly ll = new Ly.Impl(Vect2f.make(10000, 500));
		ll.ignoreTooShort.set(true);

		//a ještě jeden
		Ly ll2 = new Ly.Impl(Vect2f.make(ly.getSize().x, 500));
		ll2.ignoreTooShort.set(true);

		//vytvoříme slider a nastavíme, že hodnota na něm udává zarovnání vnitřního layoutu 'll'
		Slider.Basic sl1 = new Slider.Basic(style, Vect2f.make(300,80), Vect2f.make(50,100));
		sl1.value().getSetterListeners().add(e->ll.setAlignment(1, e.newVal.get()));

		//vytvoříme další slider, od jeho hodnoty se bude odvíjet kulatost jeho čudlíku
		Slider.Basic sl2 = new Slider.Basic(style, Vect2f.make(300,80), Vect2f.make(100,100));
		sl2.value().getSetterListeners().add(e->sl2.cudlik.base.roundness().set(e.newVal.get()));

		//přidáme prvky do vnitřního layoutu
		ll2.getDrawableChildren().add(sl1);
		ll2.getDrawableChildren().add( sl2);
		ll2.setInnerPadding(40f);				//nastavíme pěkné odsazení a zarovnání
		ll2.setAlignment(0,1f);

		//nastavíme layout přesně na velikost, kterou mají dohromady všechny jeho prvky
		ll2.setPrefSize(ll2.computeRealSize());

		//ještě vytvoříme několik ikon a přidáme je do 2. vnitřního layoutu
		Style style2 = new Style(Color.CYAN, Color.BLACK, Color.BROWN, 0.9f, 0.1f, Vect2f.make(10,10));
		ll.getDrawableChildren().add(It.make( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(250,254), style2))));
		It i4 = It.make( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(250,134), style2)));//do vnitřního layoutu přidáme nový prvek
		ll.getDrawableChildren().add(i4);
		It i5 = It.make(BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(150,100), style2)));//přidáme další prvek
		ll.getDrawableChildren().add(i5);
		ll.setInnerPadding(50f);
		ll.applySizeChanges();
		ll.setPrefSize(ll.computeRealSize()); //nastavíme layout přesně na velikost, kterou mají jeho prvky dohromady

		//oba vnitřní layouty přidáme do kořenového layoutu
		ly.getDrawableChildren().add(ll);
		ly.getDrawableChildren().add(ll2);

		//nastavíme úhledné odsazení
		ly.setInnerPadding(10f);
		ly.setOuterPadding(Vect2f.make(10,10));

		//vytvoříme manager na zpracování vstupu od uživatele
		InputManager.AsInputProcessor man = new InputManager.AsInputProcessor();
		man.getTouchConsumers().add(ly); //zaregistrujeme do něj kořenový layout
		Gdx.input.setInputProcessor(man); //zaregistujeme ho, aby dostával vstup ze systému

		return ly; //vrátíme kořenový layout
	}


}
