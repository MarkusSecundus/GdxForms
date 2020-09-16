package com.markussecundus.formsgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.markussecundus.forms.elements.DrawableElem;
import com.markussecundus.forms.elements.impl.BasicLabel;
import com.markussecundus.forms.elements.impl.ImageIcon;
import com.markussecundus.forms.elements.impl.layouts.BasicGridLayout;
import com.markussecundus.forms.elements.impl.layouts.BasicLinearLayout;
import com.markussecundus.forms.elements.impl.layouts.PrimitivePositionalLayout;
import com.markussecundus.forms.gfx.GraphicalPrimitive;
import com.markussecundus.forms.text.Font;
import com.markussecundus.forms.utils.FormsUtil;
import com.markussecundus.forms.utils.vector.Vect2d;
import com.markussecundus.forms.utils.vector.Vect2f;
import com.markussecundus.forms.utils.vector.VectUtil;
import com.markussecundus.forms.wrappers.Wrapper;
import com.markussecundus.forms.wrappers.property.ConstProperty;
import com.markussecundus.forms.wrappers.property.binding.Bindings;
import com.markussecundus.forms.wrappers.property.impl.constant.SimpleConstProperty;
import com.markussecundus.formsgdx.examples.Slider;
import com.markussecundus.formsgdx.graphics.RoundedRectangle;
import com.markussecundus.formsgdx.input.InputManager;
import com.markussecundus.formsgdx.input.mixins.IListeneredUniversalConsumer;
import com.markussecundus.formsgdx.rendering.BasicRenderer;
import com.markussecundus.formsgdx.text.FormsBitmapFont;

import java.util.ArrayList;
import java.util.List;


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

	/**
	 * Mírně spartánská implementace textového čudlíku.
	 * */
	static class TextButton extends ImageIcon<BasicRenderer, Vect2f, Float, BasicRenderer.ShapeToBasicDrw<Vect2f, Float, RoundedRectangle.SObrubou>> implements IListeneredUniversalConsumer{

		/**
		 * Vytvoří instanci čudlíku.
		 *
		 * @param img ikona, která má sloužit jako tělo čudlíku
		 * @param lbl label, který má sloužit jako popiska čudlíku
		 * @param transitionColor barva, na kterou se změní vnitřek čudlíku, když je čudlík stisknutý
		 * */
		public TextButton(RoundedRectangle.SObrubou img, BasicLabel<BasicRenderer, Vect2f, Float> lbl, Color transitionColor) {
			super(BasicRenderer.conv(img));
			this.lbl = lbl;

			//navážeme meze velikosti vnitřního labelu na velikost celého čudlíku
			Bindings.bind(lbl._sizeConstraint(), size());

			//vytáhneme z ikony barvu, jakou má mít normálně
			Color normalCol = img.getInner().getColor();

			//při kliknutí na čudlík se barva změní na transitionColor
			getOnClickedListener()._getUtilListeners().add(e->{
				img.getInner().setColor(transitionColor);
				return true;
			});
			//při odkliknutí změníme zpět na normalCol
			getOnUnclickedListener()._getUtilListeners().add(e->{
				img.getInner().setColor(normalCol);
				return true;
			});
		}

		//popiska obsahující text čudlíku
		public final BasicLabel<BasicRenderer, Vect2f, Float> lbl;

		//vypkreslí ikonu a přes ni text čudlíku
		@Override
		public boolean draw(BasicRenderer renderer, Vect2f position) {
			return super.draw(renderer, position) | lbl.draw(renderer, position);
		}
	}

	/**
	 * Primitivní utilita pro přepínání mezi několika různými prvky GUI.
	 * */
	static class Switcher extends PrimitivePositionalLayout<BasicRenderer, Vect2f, Float> implements IListeneredUniversalConsumer.ForLayout{

		public Switcher(Vect2f prefSize) {
			super(Vect2f.INF, Vect2f.ZERO, prefSize, Vect2f.getUtility());
		}

		private final ConstProperty<List<DrawableElem<BasicRenderer, Vect2f>>> childList = new SimpleConstProperty<>(new ArrayList<>());

		/**
		 * Vrátí vždy jednoprvkový list pouze s jedním právě aktivním potomkem.
		 * */
		@Override
		public ConstProperty<List<DrawableElem<BasicRenderer, Vect2f>>> drawableChildren() {
			List<DrawableElem<BasicRenderer, Vect2f>> options = optionsList().get(),
													  childList = this.childList.get();
			childList.clear();
			if(options.size()>0)
				childList.add(options.get(currIndex));
			return this.childList;
		}

		/**List prvků, mezi kterými je možno přepínat, který z nich je aktivní*/
		public ConstProperty<List<DrawableElem<BasicRenderer, Vect2f>>> optionsList(){
			return super.drawableChildren();
		}

		/**Index aktuálně aktivního prvku v rámci listu všech možností*/
		private int currIndex = 0;

		/**
		 * Posune index aktivního prvku o danou hodnotu
		 * */
		public int jumpCurr(int ammount){
			int size = optionsList().get().size();
			return currIndex = size<=0 ? 0 : FormsUtil.mod(currIndex+ ammount, size);
		}
	}

	private Switcher root;

	@Override
	public DrawableElem<BasicRenderer, Vect2f> createForm() {
		BackgroundColor = Color.GREEN;

		DrawableElem<BasicRenderer, Vect2f> formV1 = createFormV1();
		DrawableElem<BasicRenderer, Vect2f> formV2 = createFormV2();

		root = new Switcher(Vect2f.make(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

		root.optionsList().get().add(formV2);
		root.optionsList().get().add(formV1);


		InputManager.AsInputProcessor man = new InputManager.AsInputProcessor();
		man.getTouchConsumers().add(root); //zaregistrujeme do něj kořenový layout
		Gdx.input.setInputProcessor(man); //zaregistujeme ho, aby dostával vstup ze systému

		return root;
	}

	/**
	 * Druhý ukázkový formulář.
	 * <p>Náhodná změť prvků sdružených do grid-layoutu.
	 * <p>Obsahuje ukázku bindování properties a použití labelu.
	 * */
	public DrawableElem<BasicRenderer, Vect2f> createFormV2(){
		GrLy ly = new GrLy(Vect2f.getUtility()){{
			setIgnoreTooShort(true);
			setPrefSize(Vect2f.make(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
			setDefaultCellSize(Vect2f.make(150,150));
			setDefaultCellAllignment(0d, 1d);
			setCollumnSize(0, 1, 110f);
			setCollumnSize(0, 2, 200f);
			setCollumnSize(0, 3, 300f);
		}};

		It i = new It(BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(200,200), 0.6f, Vect2f.make(30,30), Color.ORANGE, Color.BROWN)));
		It i1 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(50,200), 0.6f, Vect2f.make(30,30), Color.CYAN, Color.BROWN)));
		It i2 = new It( BasicRenderer.conv(new RoundedRectangle.SObrubou(Vect2f.make(100,100), 0.9f, Vect2f.make(30,30), Color.WHITE, Color.GOLD)));


		Style style = new Style() {{
			innerColor = Color.ORANGE;
			outerColor = Color.DARK_GRAY;
			transitionColor = Color.SCARLET;
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
		//nabindujeme zarovnání 1. sloupce na hodnotu 4. slideru
		Bindings.bind(ly.collumnAllignment(0, 1),val->(double)val,  slider4.value());

		//umístíme ikony do layoutu na nějaké pozice
		ly.addDrawableChild(i, 0,0);
		ly.addDrawableChild(i1, 1,1);
		ly.addDrawableChild(i2, 1,0);

		//umístíme slidery do layoutu na nějaké pozice
		ly.addDrawableChild(slider, 2,3);
		ly.addDrawableChild(slider2, 2,2);
		ly.addDrawableChild(slider3, 0,1);
		ly.addDrawableChild(slider4, 3,1);

		//vytvoříme font, který bude používat label
		Font<BasicRenderer, Vect2f, Float> font = FormsBitmapFont.make_Vect2f(new BitmapFont(), Color.SCARLET, 1.9f);

		//vytvoříme label, který bude vypisovat informace o aktuální hodnotě fps
		Lbl lbl = new Lbl(){{
				setFont(font);
				setPrefSize(Vect2f.make(1000, 300));
				setAllignment(Vect2d.make(1d,0.5d));
			}


			//pomocné proměnné pro počítání fps
			float deltaSum = 0;
			int frameCount = 0;
			float elapsedTime=0;

			//každý snímek upravíme text labelu na aktuální hodnotu fps
			@Override public void update(float delta, int frameId) {
				super.update(delta, frameId);
				//setText(String.format("FPS: %.2f", 1f/delta));
				setText(String.format("fps: %.2f\nFPS: %.0f\navg: %.2f\n", 1f/delta, 1/((deltaSum += delta)/++frameCount), 1/((elapsedTime+=delta)/frameId)));
				if (deltaSum >0.2f){
					frameCount = 0;
					deltaSum =0;
				}
			}
		};

		ly.addDrawableChild(lbl, 2, 1);

		TextButton switchButton = new TextButton(new RoundedRectangle.SObrubou(Vect2f.make(200,200), 0.6f, Vect2f.make(30,30), Color.ORANGE, Color.BROWN),
				new Lbl(){{
					setFont(font);
					setPrefSize(Vect2f.make(500, 300));
					setAllignment(Vect2d.make(0.5d, 0.5d));
					setText("SWITCH");
				}},
				Color.FOREST
		);
		switchButton.getOnTouchUpListener().add(e-> {if(e.actor.isClicked())root.jumpCurr(1);});

		ly.addDrawableChild(switchButton, 0, 3);

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

		TextButton switchButton = new TextButton(new RoundedRectangle.SObrubou(Vect2f.make(200,100), 0.6f, Vect2f.make(30,30), Color.ORANGE, Color.BROWN),
				new Lbl(){{
					setFont(FormsBitmapFont.make_Vect2f(new BitmapFont(), Color.SCARLET, 1.9f));
					setPrefSize(Vect2f.make(500, 300));
					setAllignment(Vect2d.make(0.5d, 0.5d));
					setText("SWITCH");
				}},
				Color.FOREST
		);
		switchButton.getOnTouchUpListener().add(e->{if(e.actor.isClicked())root.jumpCurr(1);});

		ly.getDrawableChildren().add(1, switchButton);

		//ly.setPrefSize(ly.computeRealSize());


		return ly; //vrátíme kořenový layout
	}

}
