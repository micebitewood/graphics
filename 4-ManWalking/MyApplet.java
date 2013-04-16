/*
	Assinment 4
	John Mu
	jm4911@nyu.edu

	This is a scene of a man walking out of a house, with door automatically rotated for him.
*/
import java.awt.*;

public class MyApplet extends BufferedApplet
{
	int w = 0, h = 0;
	//width and height

	Geometry world = new Geometry();
	

//-------------------------------------set up a person--------------------------------------------------------------

	//let's draw a person, 
	//with joints like neck, shoulders, elbows, wrists, butts, knees, and ankles, 
	//and with objects like body, head, UpperArms, lowerArms, hands, UpperLegs, lowerLegs, and feets
	Geometry person = world.add();

	Geometry neck = person.add();

	Geometry leftShoulder = person.add();
	Geometry rightShoulder = person.add();

	Geometry leftElbow = leftShoulder.add();
	Geometry rightElbow = rightShoulder.add();

	Geometry leftWrist = leftElbow.add();
	Geometry rightWrist = rightElbow.add();

	Geometry leftButt = person.add();
	Geometry rightButt = person.add();

	Geometry leftKnee = leftButt.add();
	Geometry rightKnee = rightButt.add();

	Geometry leftAnkle = leftKnee.add();
	Geometry rightAnkle = rightKnee.add();

	Geometry body = person.add();

	Geometry head = neck.add();

	Geometry leftUArm = leftShoulder.add();
	Geometry rightUArm = rightShoulder.add();

	Geometry leftLArm = leftElbow.add();
	Geometry rightLArm = rightElbow.add();

	Geometry leftHand = leftWrist.add();
	Geometry rightHand = rightWrist.add();

	Geometry leftULeg = leftButt.add();
	Geometry rightULeg = rightButt.add();

	Geometry leftLLeg = leftKnee.add();
	Geometry rightLLeg = rightKnee.add();

	Geometry leftFoot = leftAnkle.add();
	Geometry rightFoot = rightAnkle.add();

//------------------------------set up a house-----------------------------------

	//now let's draw a house with a door and a door frame
	Geometry house = world.add();

	Geometry wall = house.add();

	Geometry doorFrame = house.add();

	Geometry doorSide = house.add();
	
	Geometry door = doorSide.add();

	//projection determines the view point
	Projection projection = new Projection();

//-------------------------------------render--------------------------------------

	public void render(Graphics g)
	{
		if(w == 0)
		{
			w = getWidth();
			h = getHeight();

			projection.setWidth(w);
			projection.setHeight(h);
			projection.setFL(15);
			g.setColor(Color.white);
			g.fillRect(0, 0, w, h);

//--------------------------------------complete the details of a person--------------------------------

			body.sphere();
			body.getMatrix().identity().scale(0.6, 1, 0.6);
			//draw body

			neck.getMatrix().identity().translate(0, 1, 0);
			//for head

			head.sphere();
			head.getMatrix().identity().translate(0, .3, 0).scale(0.3, 0.3, 0.3);
			//draw head

			leftShoulder.getMatrix().identity().translate(.5, .6, 0).rotateZ(-.8);
			//for leftUArm

			leftUArm.sphere();
			leftUArm.getMatrix().identity().translate(.5, 0, 0).scale(.5, .15, .15);
			//draw left upper arm

			leftElbow.getMatrix().identity().translate(1, 0, 0);
			//for leftLArm

			leftLArm.sphere();
			leftLArm.getMatrix().identity().translate(.5, 0, 0).scale(.5, .1, .1);
			//draw left lower arm

			leftWrist.getMatrix().identity().translate(1, 0, 0);
			//for leftHand

			leftHand.sphere();
			leftHand.getMatrix().identity().translate(.2, 0, 0).scale(.2, .2, .1);
			//draw left hand

			//codes below are almost the same with the codes above
			rightShoulder.getMatrix().identity().translate(-.5, .6, 0).rotateZ(.8);

			rightUArm.sphere();
			rightUArm.getMatrix().identity().translate(-.5, 0, 0).scale(.5, .15, .15);

			rightElbow.getMatrix().identity().translate(-1, 0, 0);

			rightLArm.sphere();
			rightLArm.getMatrix().identity().translate(-.5, 0, 0).scale(.5, .1, .1);

			rightWrist.getMatrix().identity().translate(-1, 0, 0);

			rightHand.sphere();
			rightHand.getMatrix().identity().translate(-.2, 0, 0).scale(.2, .2, .1);
			//right arm ended

			leftButt.getMatrix().identity().translate(.25, -.9, 0);
			//for leftULeg

			leftULeg.sphere();
			leftULeg.getMatrix().identity().translate(0, -.5, 0).scale(.25, .5, .25);
			//draw left upper leg

			leftKnee.getMatrix().identity().translate(0, -1, 0);
			//for leftLLeg

			leftLLeg.sphere();
			leftLLeg.getMatrix().identity().translate(0, -.5, 0).scale(.2, .5, .2);
			//draw left lower leg

			leftAnkle.getMatrix().identity().translate(0, -1, 0);
			//for leftFoot

			leftFoot.sphere();
			leftFoot.getMatrix().identity().translate(0, -.1, 0).scale(.2, .1, .4);
			//draw left foot

			//codes below are almost the same with the codes above
			rightButt.getMatrix().identity().translate(-.25, -.9, 0);

			rightULeg.sphere();
			rightULeg.getMatrix().identity().translate(0, -.5, 0).scale(.25, .5, .25);

			rightKnee.getMatrix().identity().translate(0, -1, 0);

			rightLLeg.sphere();
			rightLLeg.getMatrix().identity().translate(0, -.5, 0).scale(.2, .5, .2);

			rightAnkle.getMatrix().identity().translate(0, -1, 0);

			rightFoot.sphere();
			rightFoot.getMatrix().identity().translate(0, -.1, 0).scale(.2, .1, .4);
			//right leg ended

//------------------------------------------complete the details of the house-------------------------------

			house.getMatrix().identity().translate(4, 4, -6);

			wall.cube();
			wall.getMatrix().identity().scale(8, 8, 8);

			doorSide.getMatrix().identity().translate(-2, -8, 8);

			door.cube();
			door.getMatrix().identity().translate(2, 4, 0).scale(2, 4, .1);

			doorFrame.cube();
			doorFrame.getMatrix().identity().translate(0, -4, 8).scale(2, 4, .1);
		}

		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		//refreshing

		double time = System.currentTimeMillis() / 500.0 % 30;
		g.setColor(Color.black);
		//let's begin drawing!

		house.getMatrix().identity().rotateY(Math.PI / 3).translate(4, 4, -6);
		person.getMatrix().identity().rotateY(Math.PI / 3).translate(0, 0, time / 2 - 3.5);
		//house and person are children of world

		if(time >= 15 && time <= 20)
			doorSide.getMatrix().identity().translate(-2, -8, 8).rotateY(-1.4);
		else if(time > 5)
			doorSide.getMatrix().identity().translate(-2, -8, 8).rotateY(.7 * Math.cos((time - 5) / 4) - .7);
		else
			doorSide.getMatrix().identity().translate(-2, -8, 8);
		//simulate the door revolving

		leftShoulder.getMatrix().identity().translate(.5, .6, 0).rotateZ(-1.2).rotateY(.5 * Math.sin(time));
		rightShoulder.getMatrix().identity().translate(-.5, .6, 0).rotateZ(1.2).rotateY(.5 * Math.sin(time));
		//waving arms

		leftButt.getMatrix().identity().translate(.25, -.9, 0).rotateX(-.5 * Math.sin(time));
		rightButt.getMatrix().identity().translate(-.25, -.9, 0).rotateX(.5 * Math.sin(time));
		//moving legs

		if(Math.sin(time) >= 0)
			leftKnee.getMatrix().identity().translate(0, -1, 0).rotateX(.5 * Math.sin(time));
		else
			rightKnee.getMatrix().identity().translate(0, -1, 0).rotateX(-.5 * Math.sin(time));
		//simulate walking

		world.draw(g, projection);
	}
}
